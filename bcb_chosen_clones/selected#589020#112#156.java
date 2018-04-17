    public MoteDeploymentConfiguration addMoteDeploymentConfiguration(int projectDepConfID, int moteID, int programID, int radioPowerLevel) throws AdaptationException {
        MoteDeploymentConfiguration mdc = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "INSERT INTO MoteDeploymentConfigurations(" + "projectDeploymentConfigurationID, " + "moteID, programID, radioPowerLevel) VALUES (" + projectDepConfID + ", " + moteID + ", " + programID + ", " + radioPowerLevel + ")";
            connection = DriverManager.getConnection(CONN_STR);
            statement = connection.createStatement();
            statement.executeUpdate(query);
            query = "SELECT * from MoteDeploymentConfigurations WHERE " + "projectDeploymentConfigurationID = " + projectDepConfID + " AND moteID = " + moteID;
            resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                connection.rollback();
                String msg = "Unable to select newly added config.";
                log.error(msg);
                ;
                throw new AdaptationException(msg);
            }
            mdc = getMoteDeploymentConfiguration(resultSet);
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (Exception e) {
            }
            String msg = "SQLException in addMoteDeploymentConfiguration";
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
        return mdc;
    }
