    public Program createNewProgram(int projectID, String name, String description) throws AdaptationException {
        Program program = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(CONN_STR);
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            String query = "INSERT INTO Programs(projectID, name, " + "description, sourcePath) VALUES ( " + projectID + ", " + "'" + name + "', " + "'" + description + "', " + "'" + "[unknown]" + "')";
            log.debug("SQL Query:\n" + query);
            statement.executeUpdate(query);
            query = "SELECT * FROM Programs WHERE " + " projectID   =  " + projectID + "  AND " + " name        = '" + name + "' AND " + " description = '" + description + "'";
            resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                connection.rollback();
                String msg = "Attempt to create program failed";
                log.error(msg);
                throw new AdaptationException(msg);
            }
            program = getProgram(resultSet);
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (Exception e) {
            }
            String msg = "SQLException in createNewProgram";
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
        return program;
    }
