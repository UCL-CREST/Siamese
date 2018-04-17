    @Override
    public int deleteStatement(String sql) {
        Statement statement = null;
        try {
            statement = getConnection().createStatement();
            int result = statement.executeUpdate(sql.toString());
            if (result == 0) log.warn(sql + " result row count is 0");
            getConnection().commit();
            return result;
        } catch (SQLException e) {
            try {
                getConnection().rollback();
            } catch (SQLException e1) {
                log.error(e1.getMessage(), e1);
            }
            log.error(e.getMessage(), e);
            throw new RuntimeException();
        } finally {
            try {
                statement.close();
                getConnection().close();
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
