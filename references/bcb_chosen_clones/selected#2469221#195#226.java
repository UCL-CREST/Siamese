    public void removeResource(String resourceID, String sql, String[] keys) throws XregistryException {
        try {
            Connection connection = globalContext.createConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = null;
            try {
                statement = connection.prepareStatement(sql);
                for (int i = 0; i < keys.length; i++) {
                    statement.setString(i + 1, keys[i]);
                }
                statement.executeUpdate();
                statement = connection.prepareStatement(DELETE_RESOURCE_SQL);
                statement.setString(1, resourceID);
                statement.executeUpdate();
                log.info("Execuate SQL " + statement);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new XregistryException(e);
            } finally {
                try {
                    statement.close();
                    connection.setAutoCommit(true);
                    globalContext.closeConnection(connection);
                } catch (SQLException e) {
                    throw new XregistryException(e);
                }
            }
        } catch (SQLException e) {
            throw new XregistryException(e);
        }
    }
