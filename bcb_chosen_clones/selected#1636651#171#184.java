    public final boolean login(String user, String pass) {
        if (user == null || pass == null) return false;
        connectionInfo.setData("com.tensegrity.palojava.pass#" + user, pass);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pass.getBytes());
            pass = asHexString(md.digest());
        } catch (NoSuchAlgorithmException ex) {
            throw new PaloException("Failed to create encrypted password for " + "user '" + user + "'!", ex);
        }
        connectionInfo.setUser(user);
        connectionInfo.setPassword(pass);
        return loginInternal(user, pass);
    }
