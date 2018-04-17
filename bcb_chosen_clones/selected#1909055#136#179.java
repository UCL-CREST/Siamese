    public ProgramMessageSymbol deleteProgramMessageSymbol(int id) throws AdaptationException {
        ProgramMessageSymbol pmt = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM ProgramMessageSymbols " + "WHERE id = " + id;
            connection = DriverManager.getConnection(CONN_STR);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                String msg = "Attempt to delete program message type " + "failed.";
                log.error(msg);
                ;
                throw new AdaptationException(msg);
            }
            pmt = getProgramMessageSymbol(resultSet);
            query = "DELETE FROM ProgramMessageSymbols " + "WHERE id = " + id;
            statement.executeUpdate(query);
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (Exception e) {
            }
            String msg = "SQLException in deleteProgramMessageSymbol";
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
        return pmt;
    }
