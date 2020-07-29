    public void deleteUser(String userID) throws XregistryException {
        try {
            userID = Utils.canonicalizeDN(userID);
            Connection connection = context.createConnection();
            connection.setAutoCommit(false);
            try {
                PreparedStatement statement1 = connection.prepareStatement(DELETE_USER_SQL_MAIN);
                statement1.setString(1, userID);
                statement1.executeUpdate();
                PreparedStatement statement2 = connection.prepareStatement(DELETE_USER_SQL_DEPEND);
                statement2.setString(1, userID);
                statement2.executeUpdate();
                connection.commit();
                Collection<Group> groupList = groups.values();
                for (Group group : groupList) {
                    group.removeUser(userID);
                }
                log.info("Delete User " + userID);
            } catch (SQLException e) {
                connection.rollback();
                throw new XregistryException(e);
            } finally {
                context.closeConnection(connection);
            }
        } catch (SQLException e) {
            throw new XregistryException(e);
        }
    }
