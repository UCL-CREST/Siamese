    public ProgramMessageSymbol addProgramMessageSymbol(int programID, String name, byte[] bytecode) throws AdaptationException {
        ProgramMessageSymbol programMessageSymbol = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Statement statement = null;
        ResultSet resultSet = null;
        InputStream stream = new ByteArrayInputStream(bytecode);
        try {
            String query = "INSERT INTO ProgramMessageSymbols(programID, name, " + "bytecode) VALUES ( ?, ?, ? )";
            connection = DriverManager.getConnection(CONN_STR);
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, programID);
            preparedStatement.setString(2, name);
            preparedStatement.setBinaryStream(3, stream, bytecode.length);
            log.info("INSERT INTO ProgramMessageSymbols(programID, name, " + "bytecode) VALUES (" + programID + ", '" + name + "', " + "<bytecode>)");
            preparedStatement.executeUpdate();
            statement = connection.createStatement();
            query = "SELECT * FROM ProgramMessageSymbols WHERE " + "programID =  " + programID + " AND " + "name      = '" + name + "'";
            resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                connection.rollback();
                String msg = "Attempt to add program message symbol failed.";
                log.error(msg);
                ;
                throw new AdaptationException(msg);
            }
            programMessageSymbol = getProgramMessageSymbol(resultSet);
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (Exception e) {
            }
            String msg = "SQLException in addProgramMessageSymbol";
            log.error(msg, ex);
            throw new AdaptationException(msg, ex);
        } finally {
            try {
                resultSet.close();
            } catch (Exception ex) {
            }
            try {
                preparedStatement.close();
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
        return programMessageSymbol;
    }
