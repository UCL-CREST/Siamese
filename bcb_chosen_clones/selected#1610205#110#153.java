    public Project deleteProject(int projectID) throws AdaptationException {
        Project project = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM Projects WHERE id = " + projectID;
            connection = DriverManager.getConnection(CONN_STR);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                connection.rollback();
                String msg = "Attempt to delete project failed.";
                log.error(msg);
                throw new AdaptationException(msg);
            }
            project = getProject(resultSet);
            query = "DELETE FROM Projects WHERE id = " + projectID;
            statement.executeUpdate(query);
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (Exception e) {
            }
            String msg = "SQLException in deleteProject";
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
        return project;
    }
