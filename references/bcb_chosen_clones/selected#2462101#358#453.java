    public void setUserPassword(final List<NewUser> users) {
        try {
            final List<Integer> usersToRemoveFromCache = new ArrayList<Integer>();
            connection.setAutoCommit(false);
            new ProcessEnvelope().executeNull(new ExecuteProcessAbstractImpl(connection, false, false, true, true) {

                @Override
                public void executeProcessReturnNull() throws SQLException {
                    psImpl = connImpl.prepareStatement(sqlCommands.getProperty("user.updatePassword"));
                    Iterator<NewUser> iter = users.iterator();
                    NewUser user;
                    PasswordHasher ph;
                    while (iter.hasNext()) {
                        user = iter.next();
                        ph = PasswordFactory.getInstance().getPasswordHasher();
                        psImpl.setString(1, ph.hashPassword(user.password));
                        psImpl.setString(2, ph.getSalt());
                        psImpl.setInt(3, user.userId);
                        psImpl.executeUpdate();
                        usersToRemoveFromCache.add(user.userId);
                    }
                }
            });
            List<JESRealmUser> list = (List<JESRealmUser>) new ProcessEnvelope().executeObject(new ExecuteProcessAbstractImpl(connection, false, false, true, true) {

                @Override
                public Object executeProcessReturnObject() throws SQLException {
                    List<JESRealmUser> list = new ArrayList<JESRealmUser>(users.size() + 10);
                    psImpl = connImpl.prepareStatement(sqlCommands.getProperty("realms.user.load"));
                    Iterator<NewUser> iter = users.iterator();
                    NewUser user;
                    while (iter.hasNext()) {
                        user = iter.next();
                        psImpl.setInt(1, user.userId);
                        rsImpl = psImpl.executeQuery();
                        while (rsImpl.next()) {
                            list.add(new JESRealmUser(user.username, user.userId, rsImpl.getInt("realm_id"), rsImpl.getInt("domain_id"), user.password, rsImpl.getString("realm_name_lower_case")));
                        }
                    }
                    return list;
                }
            });
            final List<JESRealmUser> encrypted = new ArrayList<JESRealmUser>(list.size());
            Iterator<JESRealmUser> iter = list.iterator();
            JESRealmUser jesRealmUser;
            Realm realm;
            while (iter.hasNext()) {
                jesRealmUser = iter.next();
                realm = cm.getRealm(jesRealmUser.realm);
                encrypted.add(new JESRealmUser(null, jesRealmUser.userId, jesRealmUser.realmId, jesRealmUser.domainId, PasswordFactory.getInstance().getPasswordHasher().hashRealmPassword(jesRealmUser.username.toLowerCase(locale), realm.getFullRealmName().equals("null") ? "" : realm.getFullRealmName(), jesRealmUser.password), null));
            }
            new ProcessEnvelope().executeNull(new ExecuteProcessAbstractImpl(connection, false, false, true, true) {

                @Override
                public void executeProcessReturnNull() throws SQLException {
                    psImpl = connImpl.prepareStatement(sqlCommands.getProperty("realms.user.update"));
                    Iterator<JESRealmUser> iter = encrypted.iterator();
                    JESRealmUser jesRealmUser;
                    while (iter.hasNext()) {
                        jesRealmUser = iter.next();
                        psImpl.setString(1, jesRealmUser.password);
                        psImpl.setInt(2, jesRealmUser.realmId);
                        psImpl.setInt(3, jesRealmUser.userId);
                        psImpl.setInt(4, jesRealmUser.domainId);
                        psImpl.executeUpdate();
                    }
                }
            });
            connection.commit();
            cmDB.removeUsers(usersToRemoveFromCache);
        } catch (GeneralSecurityException e) {
            log.error(e);
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                }
            }
            throw new RuntimeException("Error updating Realms. Unable to continue Operation.");
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
