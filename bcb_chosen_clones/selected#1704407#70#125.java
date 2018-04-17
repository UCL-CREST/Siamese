    public static void insertTableData(Connection dest, TableMetaData tableMetaData) throws Exception {
        PreparedStatement ps = null;
        try {
            dest.setAutoCommit(false);
            String sql = "INSERT INTO " + tableMetaData.getSchema() + "." + tableMetaData.getTableName() + " (";
            for (String columnName : tableMetaData.getColumnsNames()) {
                sql += columnName + ",";
            }
            sql = sql.substring(0, sql.length() - 1);
            sql += ") VALUES (";
            for (String columnName : tableMetaData.getColumnsNames()) {
                sql += "?" + ",";
            }
            sql = sql.substring(0, sql.length() - 1);
            sql += ")";
            IOHelper.writeInfo(sql);
            ps = dest.prepareStatement(sql);
            for (Row r : tableMetaData.getData()) {
                try {
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
                            IOHelper.writeInfo(columnName + " = " + r.getRowData().get(columnName));
                            ps.setObject(param, r.getRowData().get(columnName));
                        }
                        param++;
                    }
                    if (ps.executeUpdate() != 1) {
                        dest.rollback();
                        updateTableData(dest, tableMetaData, r);
                    }
                } catch (Exception ex) {
                    try {
                        dest.rollback();
                        updateTableData(dest, tableMetaData, r);
                    } catch (Exception ex2) {
                        IOHelper.writeError("Error in update " + sql, ex2);
                    }
                }
                ps.clearParameters();
            }
            dest.commit();
            dest.setAutoCommit(true);
        } finally {
            if (ps != null) ps.close();
        }
    }
