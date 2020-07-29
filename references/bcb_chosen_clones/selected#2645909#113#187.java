    private long newIndex(String indexname) {
        Connection con = null;
        ResultSet rs = null;
        Statement stm = null;
        StringBuffer sql = new StringBuffer();
        indexname = indexname.trim().toUpperCase();
        try {
            long index = -1;
            synchronized (FormularContextPersistensImpl.class) {
                con = getConnection();
                stm = con.createStatement();
                if ((con != null) && (stm != null)) {
                    con.setAutoCommit(false);
                    sql = new StringBuffer();
                    sql.append("SELECT * FROM INDX_EC WHERE INDX_NAME='");
                    sql.append(indexname);
                    sql.append("' FOR UPDATE");
                    rs = stm.executeQuery(sql.toString());
                    if ((rs != null) && rs.next()) {
                        sql = new StringBuffer();
                        index = rs.getLong("INDX_WERT") + 1;
                        sql.append("UPDATE INDX_EC SET INDX_WERT = ");
                        sql.append(index);
                        sql.append(" WHERE INDX_NAME='");
                        sql.append(indexname);
                        sql.append("'");
                        rs.close();
                        rs = null;
                        if (stm.executeUpdate(sql.toString()) == 1) {
                            con.commit();
                        } else {
                            con.rollback();
                            index = -1;
                        }
                    } else {
                        sql = new StringBuffer();
                        sql.append("INSERT INTO INDX_EC (INDX_NAME, INDX_WERT) VALUES('");
                        sql.append(indexname);
                        sql.append("', ");
                        sql.append(1);
                        sql.append(")");
                        if (stm.executeUpdate(sql.toString()) == 1) {
                            con.commit();
                            index = 1;
                        } else {
                            con.rollback();
                        }
                    }
                }
            }
            return index;
        } catch (Exception e) {
            Log.getLogger().error("Error during execute SQL-Statement: " + sql.toString(), e);
            return -1;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ignore) {
                }
            }
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception ignore) {
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ignore) {
                }
            }
        }
    }
