    public ProjectDeploymentConfiguration createNewProjectDeploymentConfig(int projectID, String name, String description) throws AdaptationException {
        ProjectDeploymentConfiguration config = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "INSERT INTO ProjectDeploymentConfigurations" + "(projectID, name, description) VALUES (" + projectID + ", '" + name + "', '" + description + "')";
            connection = DriverManager.getConnection(CONN_STR);
            statement = connection.createStatement();
            statement.executeUpdate(query);
            query = "SELECT * FROM ProjectDeploymentConfigurations WHERE " + " projectID   = " + projectID + "  AND " + " name        = '" + name + "' AND " + " description = '" + description + "'";
            resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                connection.rollback();
                String msg = "Attempt to create " + "ProjectDeploymentConfiguration failed.";
                log.error(msg);
                throw new AdaptationException(msg);
            }
            config = getProjectDeploymentConfiguration(resultSet);
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (Exception e) {
            }
            String msg = "SQLException in createNewProjectDeploymentConfig";
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
