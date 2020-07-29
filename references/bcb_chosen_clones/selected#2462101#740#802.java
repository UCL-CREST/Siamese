    public void removeUserFromRealm(final List<NewUser> users) {
        try {
            connection.setAutoCommit(false);
            final List<Integer> removeFromNullRealm = (List<Integer>) new ProcessEnvelope().executeObject(new ExecuteProcessAbstractImpl(connection, false, false, true, true) {

                @Override
                public Object executeProcessReturnObject() throws SQLException {
                    psImpl = connImpl.prepareStatement(sqlCommands.getProperty("realm.removeUser"));
                    Iterator<NewUser> iter = users.iterator();
                    NewUser user;
                    int realmId;
                    Iterator<Integer> iter2;
                    List<Integer> removeFromNullRealm = new ArrayList<Integer>();
                    while (iter.hasNext()) {
                        user = iter.next();
                        psImpl.setInt(1, user.userId);
                        iter2 = user.realmIds.iterator();
                        while (iter2.hasNext()) {
                            realmId = iter2.next();
                            if (realmId == 0) {
                                removeFromNullRealm.add(user.userId);
                                continue;
                            }
                            psImpl.setInt(2, realmId);
                            psImpl.executeUpdate();
                        }
                        cmDB.removeUser(user.userId);
                    }
                    return removeFromNullRealm;
                }
            });
            if (!removeFromNullRealm.isEmpty()) {
                new ProcessEnvelope().executeNull(new ExecuteProcessAbstractImpl(connection, false, false, true, true) {

                    @Override
                    public void executeProcessReturnNull() throws SQLException {
                        psImpl = connImpl.prepareStatement(sqlCommands.getProperty("realm.removeUserFromNullRealm"));
                        Iterator<Integer> iter2 = removeFromNullRealm.iterator();
                        while (iter2.hasNext()) {
                            psImpl.setInt(1, iter2.next());
                            psImpl.executeUpdate();
                        }
                    }
                });
            }
            connection.commit();
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
