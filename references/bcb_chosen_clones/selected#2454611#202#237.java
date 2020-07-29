            public Object run() {
                List correctUsers = (List) JsonPath.query("select * from ? where name=?", usersTable(), username);
                if (correctUsers.size() == 0) {
                    return new LoginException("user " + username + " not found");
                }
                Persistable userObject = (Persistable) correctUsers.get(0);
                boolean alreadyHashed = false;
                boolean passwordMatch = password.equals(userObject.get(PASSWORD_FIELD));
                if (!passwordMatch) {
                    try {
                        MessageDigest md = MessageDigest.getInstance("SHA");
                        md.update(((String) userObject.get(PASSWORD_FIELD)).getBytes());
                        passwordMatch = password.equals(new String(new Base64().encode(md.digest())));
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }
                    alreadyHashed = true;
                }
                if (passwordMatch) {
                    Logger.getLogger(User.class.toString()).info("User " + username + " has been authenticated");
                    User user = (User) userObject;
                    try {
                        if (alreadyHashed) user.currentTicket = password; else {
                            MessageDigest md = MessageDigest.getInstance("SHA");
                            md.update(password.getBytes());
                            user.currentTicket = new String(new Base64().encode(md.digest()));
                        }
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }
                    return user;
                } else {
                    Logger.getLogger(User.class.toString()).info("The password was incorrect for " + username);
                    return new LoginException("The password was incorrect for user " + username + ". ");
                }
            }
