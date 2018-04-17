    public ProgramProfilingSymbol updateProgramProfilingSymbol(int id, int configID, int programSymbolID) throws AdaptationException {
        ProgramProfilingSymbol pps = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "UPDATE ProgramProfilingSymbols SET " + "projectDeploymentConfigurationID = " + configID + ", " + "programSymbolID                  = " + programSymbolID + ", " + "WHERE id = " + id;
            connection = DriverManager.getConnection(CONN_STR);
            statement = connection.createStatement();
            statement.executeUpdate(query);
            query = "SELECT * from ProgramProfilingSymbols WHERE " + "id = " + id;
            resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                connection.rollback();
                String msg = "Attempt to update program profiling " + "symbol failed.";
                log.error(msg);
                throw new AdaptationException(msg);
            }
            pps = getProfilingSymbol(resultSet);
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (Exception e) {
            }
            String msg = "SQLException in updateProgramProfilingSymbol";
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
        return pps;
    }
