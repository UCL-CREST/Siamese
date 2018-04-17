    public boolean save(String trxName) {
        if (m_value == null || (!(m_value instanceof String || m_value instanceof byte[])) || (m_value instanceof String && m_value.toString().length() == 0) || (m_value instanceof byte[] && ((byte[]) m_value).length == 0)) {
            StringBuffer sql = new StringBuffer("UPDATE ").append(m_tableName).append(" SET ").append(m_columnName).append("=null WHERE ").append(m_whereClause);
            int no = DB.executeUpdate(sql.toString(), trxName);
            log.fine("save [" + trxName + "] #" + no + " - no data - set to null - " + m_value);
            if (no == 0) log.warning("[" + trxName + "] - not updated - " + sql);
            return true;
        }
        StringBuffer sql = new StringBuffer("UPDATE ").append(m_tableName).append(" SET ").append(m_columnName).append("=? WHERE ").append(m_whereClause);
        boolean success = true;
        if (DB.isRemoteObjects()) {
            log.fine("[" + trxName + "] - Remote - " + m_value);
            Server server = CConnection.get().getServer();
            try {
                if (server != null) {
                    success = server.updateLOB(sql.toString(), m_displayType, m_value, trxName, SecurityToken.getInstance());
                    if (CLogMgt.isLevelFinest()) log.fine("server.updateLOB => " + success);
                    return success;
                }
                log.log(Level.SEVERE, "AppsServer not found");
            } catch (RemoteException ex) {
                log.log(Level.SEVERE, "AppsServer error", ex);
            }
            return false;
        }
        log.fine("[" + trxName + "] - Local - " + m_value);
        Trx trx = null;
        if (trxName != null) trx = Trx.get(trxName, false);
        Connection con = null;
        if (trx != null) con = trx.getConnection();
        if (con == null) con = DB.createConnection(false, Connection.TRANSACTION_READ_COMMITTED);
        if (con == null) {
            log.log(Level.SEVERE, "Could not get Connection");
            return false;
        }
        PreparedStatement pstmt = null;
        success = true;
        try {
            pstmt = con.prepareStatement(sql.toString());
            if (m_displayType == DisplayType.TextLong) pstmt.setString(1, (String) m_value); else pstmt.setBytes(1, (byte[]) m_value);
            int no = pstmt.executeUpdate();
            if (no != 1) {
                log.warning("[" + trxName + "] - Not updated #" + no + " - " + sql);
                success = false;
            }
        } catch (Throwable e) {
            log.log(Level.SEVERE, "[" + trxName + "] - " + sql, e);
            success = false;
        } finally {
            DB.close(pstmt);
            pstmt = null;
        }
        if (success) {
            if (trx != null) {
                trx = null;
                con = null;
            } else {
                try {
                    con.commit();
                } catch (Exception e) {
                    log.log(Level.SEVERE, "[" + trxName + "] - commit ", e);
                    success = false;
                } finally {
                    try {
                        con.close();
                    } catch (SQLException e) {
                    }
                    con = null;
                }
            }
        }
        if (!success) {
            log.severe("[" + trxName + "] - rollback");
            if (trx != null) {
                trx.rollback();
                trx = null;
                con = null;
            } else {
                try {
                    con.rollback();
                } catch (Exception ee) {
                    log.log(Level.SEVERE, "[" + trxName + "] - rollback", ee);
                } finally {
                    try {
                        con.close();
                    } catch (SQLException e) {
                    }
                    con = null;
                }
            }
        }
        return success;
    }
