    private final String encryptPassword(String pass) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            log.log(Level.WARNING, "Error while obtaining decript algorithm", e);
            throw new RuntimeException("AccountData.encryptPassword()");
        }
        try {
            md.update(pass.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.log(Level.WARNING, "Problem with decript algorithm occured.", e);
            throw new RuntimeException("AccountData.encryptPassword()");
        }
        return new BASE64Encoder().encode(md.digest());
    }
