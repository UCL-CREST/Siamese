    public MoteDeploymentConfiguration deleteMoteDeploymentConfiguration(int id) throws AdaptationException {
        MoteDeploymentConfiguration mdc = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM MoteDeploymentConfigurations " + "WHERE id = " + id;
            connection = DriverManager.getConnection(CONN_STR);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                String msg = "Unable to select config to delete.";
                log.error(msg);
                throw new AdaptationException(msg);
            }
            mdc = getMoteDeploymentConfiguration(resultSet);
            query = "DELETE FROM MoteDeploymentConfigurations " + "WHERE id = " + id;
            statement.executeUpdate(query);
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (Exception e) {
            }
            String msg = "SQLException in deleteMoteDeploymentConfiguration";
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
