    public void addUserToRealm(final NewUser user) {
        try {
            connection.setAutoCommit(false);
            final String pass, salt;
            final List<RealmWithEncryptedPass> realmPass = new ArrayList<RealmWithEncryptedPass>();
            Realm realm;
            String username;
            username = user.username.toLowerCase(locale);
            PasswordHasher ph = PasswordFactory.getInstance().getPasswordHasher();
            pass = ph.hashPassword(user.password);
            salt = ph.getSalt();
            realmPass.add(new RealmWithEncryptedPass(cm.getRealm("null"), PasswordFactory.getInstance().getPasswordHasher().hashRealmPassword(username, "", user.password)));
            if (user.realms != null) {
                for (String realmName : user.realms) {
                    realm = cm.getRealm(realmName);
                    realmPass.add(new RealmWithEncryptedPass(realm, PasswordFactory.getInstance().getPasswordHasher().hashRealmPassword(username, realm.getFullRealmName(), user.password)));
                }
                user.realms = null;
            }
            new ProcessEnvelope().executeNull(new ExecuteProcessAbstractImpl(connection, false, false, true, true) {

                @Override
                public void executeProcessReturnNull() throws SQLException {
                    psImpl = connImpl.prepareStatement(sqlCommands.getProperty("user.updatePassword"));
                    psImpl.setString(1, pass);
                    psImpl.setString(2, salt);
                    psImpl.setInt(3, user.userId);
                    psImpl.executeUpdate();
                }
            });
            new ProcessEnvelope().executeNull(new ExecuteProcessAbstractImpl(connection, false, false, true, true) {

                @Override
                public void executeProcessReturnNull() throws SQLException {
                    psImpl = connImpl.prepareStatement(sqlCommands.getProperty("realm.addUser"));
                    RealmWithEncryptedPass rwep;
                    RealmDb realm;
                    Iterator<RealmWithEncryptedPass> iter1 = realmPass.iterator();
                    while (iter1.hasNext()) {
                        rwep = iter1.next();
                        realm = (RealmDb) rwep.realm;
                        psImpl.setInt(1, realm.getRealmId());
                        psImpl.setInt(2, user.userId);
                        psImpl.setInt(3, user.domainId);
                        psImpl.setString(4, rwep.password);
                        psImpl.executeUpdate();
                    }
                }
            });
            connection.commit();
            cmDB.removeUser(user.userId);
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
