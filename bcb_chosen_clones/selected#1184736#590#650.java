    public boolean updateLOB(String sql, int displayType, Object value, String trxName, SecurityToken token) {
        validateSecurityToken(token);
        if (sql == null || value == null) {
            log.fine("No sql or data");
            return false;
        }
        log.fine(sql);
        Trx trx = null;
        if (trxName != null && trxName.trim().length() > 0) {
            trx = Trx.get(trxName, false);
            if (trx == null) throw new RuntimeException("Transaction lost - " + trxName);
        }
        m_updateLOBCount++;
        boolean success = true;
        Connection con = trx != null ? trx.getConnection() : DB.createConnection(false, Connection.TRANSACTION_READ_COMMITTED);
        PreparedStatement pstmt = null;
        try {
            pstmt = con.prepareStatement(sql);
            if (displayType == DisplayType.TextLong) pstmt.setString(1, (String) value); else pstmt.setBytes(1, (byte[]) value);
            int no = pstmt.executeUpdate();
        } catch (Exception e) {
            log.log(Level.FINE, sql, e);
            success = false;
        } finally {
            DB.close(pstmt);
            pstmt = null;
        }
        if (success && trx == null) {
            try {
                con.commit();
            } catch (Exception e) {
                log.log(Level.SEVERE, "commit", e);
                success = false;
            } finally {
                try {
                    con.close();
                } catch (SQLException e) {
                }
                con = null;
            }
        }
        if (!success) {
            log.severe("rollback");
            if (trx == null) {
                try {
                    con.rollback();
                } catch (Exception ee) {
                    log.log(Level.SEVERE, "rollback", ee);
                } finally {
                    try {
                        con.close();
                    } catch (SQLException e) {
                    }
                    con = null;
                }
            } else {
                trx.rollback();
            }
        }
        return success;
    }
