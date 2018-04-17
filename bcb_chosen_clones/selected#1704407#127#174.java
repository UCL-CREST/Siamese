    public static void updateTableData(Connection dest, TableMetaData tableMetaData) throws Exception {
        PreparedStatement ps = null;
        try {
            dest.setAutoCommit(false);
            String sql = "UPDATE " + tableMetaData.getSchema() + "." + tableMetaData.getTableName() + " SET ";
            for (String columnName : tableMetaData.getColumnsNames()) {
                sql += columnName + " = ? ,";
            }
            sql = sql.substring(0, sql.length() - 1);
            sql += " WHERE ";
            for (String pkColumnName : tableMetaData.getPkColumns()) {
                sql += pkColumnName + " = ? AND ";
            }
            sql = sql.substring(0, sql.length() - 4);
            ps = dest.prepareStatement(sql);
            for (Row r : tableMetaData.getData()) {
                int param = 1;
                for (String columnName : tableMetaData.getColumnsNames()) {
                    if (dest instanceof OracleConnection) {
                        if (tableMetaData.getColumnsTypes().get(columnName).equalsIgnoreCase("BLOB")) {
                            BLOB blob = new BLOB((OracleConnection) dest, (byte[]) r.getRowData().get(columnName));
                            ((OraclePreparedStatement) ps).setBLOB(param, blob);
                        } else if (tableMetaData.getColumnsTypes().get(columnName).equalsIgnoreCase("CLOB")) {
                            ((OraclePreparedStatement) ps).setStringForClob(param, (String) r.getRowData().get(columnName));
                        } else if (tableMetaData.getColumnsTypes().get(columnName).equalsIgnoreCase("LONG")) {
                            ps.setBytes(param, (byte[]) r.getRowData().get(columnName));
                        }
                    } else {
                        ps.setObject(param, r.getRowData().get(columnName));
                    }
                    param++;
                }
                for (String pkColumnName : tableMetaData.getPkColumns()) {
                    ps.setObject(param, r.getRowData().get(pkColumnName));
                    param++;
                }
                if (ps.executeUpdate() != 1) {
                    dest.rollback();
                    throw new Exception();
                }
                ps.clearParameters();
            }
            dest.commit();
            dest.setAutoCommit(true);
        } finally {
            if (ps != null) ps.close();
        }
    }
