    private void updateService(int nodeID, String interfaceIP, int serviceID, String notifyFlag) throws ServletException {
        Connection connection = null;
        final DBUtils d = new DBUtils(getClass());
        try {
            connection = Vault.getDbConnection();
            d.watch(connection);
            PreparedStatement stmt = connection.prepareStatement(UPDATE_SERVICE);
            d.watch(stmt);
            stmt.setString(1, notifyFlag);
            stmt.setInt(2, nodeID);
            stmt.setString(3, interfaceIP);
            stmt.setInt(4, serviceID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException sqlEx) {
                throw new ServletException("Couldn't roll back update to service " + serviceID + " on interface " + interfaceIP + " notify as " + notifyFlag + " in the database.", sqlEx);
            }
            throw new ServletException("Error when updating to service " + serviceID + " on interface " + interfaceIP + " notify as " + notifyFlag + " in the database.", e);
        } finally {
            d.cleanUp();
        }
    }
