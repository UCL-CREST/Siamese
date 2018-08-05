    public ProgramSymbol createNewProgramSymbol(int programID, String module, String symbol, int address, int size) throws AdaptationException {
        ProgramSymbol programSymbol = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "INSERT INTO ProgramSymbols " + "(programID, module, symbol, address, size)" + " VALUES (" + programID + ", '" + module + "',  '" + symbol + "', " + address + ", " + size + ")";
            connection = DriverManager.getConnection(CONN_STR);
            statement = connection.createStatement();
            statement.executeUpdate(query);
            query = "SELECT * FROM ProgramSymbols WHERE  " + "programID =  " + programID + "  AND " + "module    = '" + module + "' AND " + "symbol    = '" + symbol + "'";
            resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                connection.rollback();
                String msg = "Attempt to create program symbol failed.";
                log.error(msg);
                throw new AdaptationException(msg);
            }
            programSymbol = getProgramSymbol(resultSet);
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (Exception e) {
            }
            String msg = "SQLException in createNewProgramSymbol";
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
