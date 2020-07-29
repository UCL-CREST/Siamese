    public ProjectDeploymentConfiguration deleteProjectDeploymentConfig(int id) throws AdaptationException {
        ProjectDeploymentConfiguration config = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM ProjectDeploymentConfigurations " + "WHERE id = " + id;
            connection = DriverManager.getConnection(CONN_STR);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                connection.rollback();
                String msg = "Attempt to delete project deployment " + "configuration failed.";
                log.error(msg);
                throw new AdaptationException(msg);
            }
            config = getProjectDeploymentConfiguration(resultSet);
            query = "DELETE FROM ProjectDeploymentConfigurations " + "WHERE id = " + id;
            statement.executeUpdate(query);
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (Exception e) {
            }
            String msg = "SQLException in deleteProjectDeploymentConfig";
            log.error(msg, ex);
            throw new AdaptationException(msg, ex);
        } finally {
            try {
                resultSet.close();
            } catch (Exception ex) {
            }
            try {
                statement.close();
            } catch (Exception ex) {
            }
            try {
                connection.close();
            } catch (Exception ex) {
            }
        }
        return config;
    }
