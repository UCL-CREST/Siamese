    protected void update(String sql, Object[] args) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = JdbcUtils.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            this.setParameters(pstmt, args);
            pstmt.executeUpdate();
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                    conn.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new JdbcDaoException(e.getMessage(), e);
        } finally {
            JdbcUtils.free(pstmt, conn);
        }
    }
