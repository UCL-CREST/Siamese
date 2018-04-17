    public void add(String user, String pass, boolean admin, boolean developer) throws FidoDatabaseException {
        try {
            Connection conn = null;
            Statement stmt = null;
            try {
                conn = FidoDataSource.getConnection();
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                String sql;
                if (contains(stmt, user) == true) {
                    sql = "update Principals set Password = '" + pass + "' " + " where PrincipalId = '" + user + "'";
                } else {
                    sql = "insert into Principals (PrincipalId, Password) " + " values ('" + user + "', '" + pass + "')";
                }
                stmt.executeUpdate(sql);
                updateRoles(stmt, user, admin, developer);
                conn.commit();
            } catch (SQLException e) {
                if (conn != null) conn.rollback();
                throw e;
            } finally {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            }
        } catch (SQLException e) {
            throw new FidoDatabaseException(e);
        }
    }
