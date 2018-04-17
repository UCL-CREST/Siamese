    public ProgramProfilingMessageSymbol createNewProfilingMessageSymbol(int configID, int programMessageSymbolID) throws AdaptationException {
        ProgramProfilingMessageSymbol profilingMessageSymbol = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "INSERT INTO ProgramProfilingMessageSymbols" + "(projectDeploymentConfigurationID, programMessageSymbolID)" + " VALUES (" + configID + ", " + programMessageSymbolID + ")";
            connection = DriverManager.getConnection(CONN_STR);
            statement = connection.createStatement();
            statement.executeUpdate(query);
            query = "SELECT * FROM ProgramProfilingMessageSymbols WHERE " + "projectDeploymentConfigurationID = " + configID + " AND " + "programMessageSymbolID           = " + programMessageSymbolID;
            resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                connection.rollback();
                String msg = "Attempt to create program profiling message " + "symbol failed.";
                log.error(msg);
                throw new AdaptationException(msg);
            }
            profilingMessageSymbol = getProfilingMessageSymbol(resultSet);
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (Exception e) {
            }
            String msg = "SQLException in createNewProfilingMessageSymbol";
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
        return profilingMessageSymbol;
    }
