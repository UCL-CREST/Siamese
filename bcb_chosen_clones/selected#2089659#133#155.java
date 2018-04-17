    @Override
    public int updateStatement(String sql) {
        Statement statement = null;
        try {
            statement = getConnection().createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            try {
                getConnection().rollback();
            } catch (SQLException e1) {
                log.error(e1.getMessage(), e1);
            }
            log.error(e.getMessage(), e);
            return 0;
        } finally {
            try {
                statement.close();
                getConnection().close();
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
