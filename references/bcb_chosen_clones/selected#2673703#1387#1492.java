    public static boolean copyDataToNewTable(EboContext p_eboctx, String srcTableName, String destTableName, String where, boolean log, int mode) throws boRuntimeException {
        srcTableName = srcTableName.toUpperCase();
        destTableName = destTableName.toUpperCase();
        Connection cn = null;
        Connection cndef = null;
        boolean ret = false;
        try {
            boolean srcexists = false;
            boolean destexists = false;
            final InitialContext ic = new InitialContext();
            cn = p_eboctx.getConnectionData();
            cndef = p_eboctx.getConnectionDef();
            PreparedStatement pstm = cn.prepareStatement("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE upper(TABLE_NAME)=?  AND TABLE_SCHEMA=database()");
            pstm.setString(1, srcTableName);
            ResultSet rslt = pstm.executeQuery();
            if (rslt.next()) {
                srcexists = true;
            }
            rslt.close();
            pstm.setString(1, destTableName);
            rslt = pstm.executeQuery();
            if (rslt.next()) {
                destexists = true;
            }
            if (!destexists) {
                rslt.close();
                pstm.close();
                pstm = cn.prepareStatement("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.VIEWS WHERE upper(TABLE_NAME)=? AND TABLE_SCHEMA=database()");
                pstm.setString(1, destTableName);
                rslt = pstm.executeQuery();
                if (rslt.next()) {
                    CallableStatement cstm = cn.prepareCall("DROP VIEW " + destTableName);
                    cstm.execute();
                    cstm.close();
                }
            }
            rslt.close();
            pstm.close();
            if (srcexists && !destexists) {
                if (log) {
                    logger.finest(LoggerMessageLocalizer.getMessage("CREATING_AND_COPY_DATA_FROM") + " [" + srcTableName + "] " + LoggerMessageLocalizer.getMessage("TO") + " [" + destTableName + "]");
                }
                CallableStatement cstm = cn.prepareCall("CREATE TABLE " + destTableName + " AS SELECT * FROM " + srcTableName + " " + (((where != null) && (where.length() > 0)) ? (" WHERE " + where) : ""));
                cstm.execute();
                cstm.close();
                if (log) {
                    logger.finest(LoggerMessageLocalizer.getMessage("UPDATING_NGTDIC"));
                }
                cn.commit();
                ret = true;
            } else if (srcexists && destexists) {
                if (log) {
                    logger.finest(LoggerMessageLocalizer.getMessage("COPY_DATA_FROM") + " [" + srcTableName + "] " + LoggerMessageLocalizer.getMessage("TO") + " [" + destTableName + "]");
                }
                PreparedStatement pstm2 = cn.prepareStatement("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE upper(TABLE_NAME) = ? AND TABLE_SCHEMA=database()");
                pstm2.setString(1, destTableName);
                ResultSet rslt2 = pstm2.executeQuery();
                StringBuffer fields = new StringBuffer();
                PreparedStatement pstm3 = cn.prepareStatement("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE upper(TABLE_NAME) = ? and upper(COLUMN_NAME)=? AND TABLE_SCHEMA=database()");
                while (rslt2.next()) {
                    pstm3.setString(1, srcTableName);
                    pstm3.setString(2, rslt2.getString(1));
                    ResultSet rslt3 = pstm3.executeQuery();
                    if (rslt3.next()) {
                        if (fields.length() > 0) {
                            fields.append(',');
                        }
                        fields.append('"').append(rslt2.getString(1)).append('"');
                    }
                    rslt3.close();
                }
                pstm3.close();
                rslt2.close();
                pstm2.close();
                CallableStatement cstm;
                int recs = 0;
                if ((mode == 0) || (mode == 1)) {
                    cstm = cn.prepareCall("INSERT INTO " + destTableName + "( " + fields.toString() + " ) ( SELECT " + fields.toString() + " FROM " + srcTableName + " " + (((where != null) && (where.length() > 0)) ? (" WHERE " + where) : "") + ")");
                    recs = cstm.executeUpdate();
                    cstm.close();
                    if (log) {
                        logger.finest(LoggerMessageLocalizer.getMessage("DONE") + " [" + recs + "] " + LoggerMessageLocalizer.getMessage("RECORDS_COPIED"));
                    }
                }
                cn.commit();
                ret = true;
            }
        } catch (Exception e) {
            try {
                cn.rollback();
            } catch (Exception z) {
                throw new boRuntimeException("boBuildDB.moveTable", "BO-1304", z);
            }
            throw new boRuntimeException("boBuildDB.moveTable", "BO-1304", e);
        } finally {
            try {
                cn.close();
            } catch (Exception e) {
            }
            try {
                cndef.close();
            } catch (Exception e) {
            }
        }
        return ret;
    }
