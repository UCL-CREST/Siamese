    public Project createProject(int testbedID, String name, String description) throws AdaptationException {
        Project project = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "INSERT INTO Projects(testbedID, name, " + "description) VALUES (" + testbedID + ", '" + name + "', '" + description + "')";
            connection = DriverManager.getConnection(CONN_STR);
            statement = connection.createStatement();
            statement.executeUpdate(query);
            query = "SELECT * FROM Projects WHERE " + " testbedID   = " + testbedID + "  AND " + " name        = '" + name + "' AND " + " description = '" + description + "'";
            resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                connection.rollback();
                String msg = "Attempt to create project failed.";
                log.error(msg);
                throw new AdaptationException(msg);
            }
            project = getProject(resultSet);
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (Exception e) {
            }
            String msg = "SQLException in createProject";
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
