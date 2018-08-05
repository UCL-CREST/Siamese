    public void insertRealm(final List<NewRealms> newRealms) {
        try {
            connection.setAutoCommit(false);
            new ProcessEnvelope().executeNull(new ExecuteProcessAbstractImpl(connection, false, false, true, true) {

                @Override
                public void executeProcessReturnNull() throws SQLException {
                    psImpl = connImpl.prepareStatement(sqlCommands.getProperty("realm.add"));
                    Iterator<NewRealms> iter = newRealms.iterator();
                    NewRealms newRealm;
                    String realm;
                    Iterator<String> iter2;
                    while (iter.hasNext()) {
                        newRealm = iter.next();
                        psImpl.setInt(3, newRealm.domainId);
                        iter2 = newRealm.realms.iterator();
                        while (iter2.hasNext()) {
                            realm = iter2.next();
                            psImpl.setString(1, realm);
                            psImpl.setString(2, realm.toLowerCase(locale));
                            psImpl.executeUpdate();
                        }
                    }
                }
            });
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
