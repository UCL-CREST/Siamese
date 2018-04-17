    public int addCollectionInstruction() throws FidoDatabaseException {
        try {
            Connection conn = null;
            Statement stmt = null;
            try {
                String sql = "insert into Instructions (Type, Operator) " + "values (1, 0)";
                conn = fido.util.FidoDataSource.getConnection();
                stmt = conn.createStatement();
                stmt.executeUpdate(sql);
                return getCurrentId(stmt);
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
