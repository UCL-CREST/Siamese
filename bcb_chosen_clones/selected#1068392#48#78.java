    public void deleteObject(String id) throws SQLException {
        boolean selfConnection = true;
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            if (dbConnection == null) {
                DatabaseConn dbConn = new DatabaseConn();
                conn = dbConn.getConnection();
                conn.setAutoCommit(false);
            } else {
                conn = dbConnection;
                selfConnection = false;
            }
            stmt = conn.prepareStatement(this.deleteSql);
            stmt.setString(1, id);
            stmt.executeUpdate();
            if (selfConnection) conn.commit();
        } catch (Exception e) {
            if (selfConnection && conn != null) conn.rollback();
            throw new SQLException(e.getMessage());
        } finally {
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
            if (selfConnection && conn != null) {
                conn.close();
                conn = null;
            }
        }
    }
