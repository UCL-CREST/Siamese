    public Program deleteProgram(int id) throws AdaptationException {
        Program program = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM Programs WHERE id = " + id;
            connection = DriverManager.getConnection(CONN_STR);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                connection.rollback();
                String msg = "Attempt to delete program failed.";
                log.error(msg);
                throw new AdaptationException(msg);
            }
            program = getProgram(resultSet);
            query = "DELETE FROM Programs WHERE id = " + id;
            statement.executeUpdate(query);
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (Exception e) {
            }
            String msg = "SQLException in deleteProgram";
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
