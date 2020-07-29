    public void executeUpdateTransaction(List queries) throws SQLException {
        assert connection != null;
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try {
            Iterator iterator = queries.iterator();
            while (iterator.hasNext()) {
                String query = (String) iterator.next();
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
            }
            connection.commit();
            connection.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException(e.getMessage());
        }
    }
