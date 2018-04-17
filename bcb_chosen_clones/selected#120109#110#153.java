    public ProgramSymbol deleteProgramSymbol(int id) throws AdaptationException {
        ProgramSymbol programSymbol = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM ProgramSymbols " + "WHERE id = " + id;
            connection = DriverManager.getConnection(CONN_STR);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                connection.rollback();
                String msg = "Attempt to delete program symbol failed.";
                log.error(msg);
                throw new AdaptationException(msg);
            }
            programSymbol = getProgramSymbol(resultSet);
            query = "DELETE FROM ProgramSymbols " + "WHERE id = " + id;
            statement.executeUpdate(query);
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (Exception e) {
            }
            String msg = "SQLException in deleteProgramSymbol";
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
        return programSymbol;
    }
