    public void removeForwardAddress(final List<NewUser> forwardAddresses) {
        try {
            final List<Integer> usersToRemoveFromCache = new ArrayList<Integer>();
            connection.setAutoCommit(false);
            new ProcessEnvelope().executeNull(new ExecuteProcessAbstractImpl(connection, false, false, true, true) {

                @Override
                public void executeProcessReturnNull() throws SQLException {
                    psImpl = connImpl.prepareStatement(sqlCommands.getProperty("userForwardAddresses.delete"));
                    Iterator<NewUser> iter = forwardAddresses.iterator();
                    Iterator<Integer> iter2;
                    NewUser newUser;
                    while (iter.hasNext()) {
                        newUser = iter.next();
                        iter2 = newUser.forwardAddressIds.iterator();
                        while (iter2.hasNext()) {
                            psImpl.setInt(1, iter2.next());
                            psImpl.executeUpdate();
                        }
                        usersToRemoveFromCache.add(newUser.userId);
                    }
                }
            });
            connection.commit();
            cmDB.removeUsers(usersToRemoveFromCache);
        } catch (SQLException sqle) {
            log.error(sqle);
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                }
            }
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ex) {
                }
            }
        }
    }
