    @Override
    public boolean identification(String username, String password) {
        this.getLogger().info(DbUserServiceImpl.class, ">>>identification " + username + "<<<");
        try {
            IFeelerUser user = this.getDbServ().queryFeelerUser(username);
            if (user == null) {
                return false;
            }
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(password.getBytes());
            if (user.getPassword().equals(new String(md5.digest()))) {
                if (!this.localUUIDList.contains(user.getUuid())) {
                    this.localUUIDList.add(user.getUuid());
                }
                return true;
            } else {
                return false;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
