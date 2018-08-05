    protected N save(String sql, Object[] args) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            this.setParameters(pstmt, args);
            pstmt.executeUpdate();
            conn.commit();
            conn.setAutoCommit(true);
            rs = pstmt.getGeneratedKeys();
            return (N) rs.getObject(1);
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
            JdbcUtils.free(rs, pstmt, conn);
        }
    }
