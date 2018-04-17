    public boolean createUser(String username, String password, String name) throws Exception {
        boolean user_created = false;
        try {
            statement = connect.prepareStatement("SELECT COUNT(*) from toepen.users WHERE username = ? LIMIT 1");
            statement.setString(1, username);
            resultSet = statement.executeQuery();
            resultSet.next();
            if (resultSet.getInt(1) == 0) {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(password.getBytes());
                BigInteger hash = new BigInteger(1, md5.digest());
                String password_hash = hash.toString(16);
                long ctime = System.currentTimeMillis() / 1000;
                statement = connect.prepareStatement("INSERT INTO toepen.users " + "(username, password, name, ctime) " + "VALUES (?, ?, ?, ?)");
                statement.setString(1, username);
                statement.setString(2, password_hash);
                statement.setString(3, name);
                statement.setLong(4, ctime);
                if (statement.executeUpdate() > 0) {
                    user_created = true;
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            close();
            return user_created;
        }
    }
