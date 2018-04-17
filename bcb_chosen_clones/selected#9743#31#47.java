    @Override
    public boolean register(String username, String password) {
        this.getLogger().info(DbUserServiceImpl.class, ">>>rigister " + username + "<<<");
        try {
            if (this.getDbServ().queryFeelerUser(username) != null) {
                return false;
            }
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(password.getBytes());
            String passwordMd5 = new String(md5.digest());
            this.getDbServ().addFeelerUser(username, passwordMd5);
            return this.identification(username, password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }
