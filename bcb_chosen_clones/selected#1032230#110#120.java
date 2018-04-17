    public static String getEncryptedPassword(String clearTextPassword) {
        if (StringUtil.isEmpty(clearTextPassword)) return "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(clearTextPassword.getBytes());
            return new sun.misc.BASE64Encoder().encode(md.digest());
        } catch (NoSuchAlgorithmException e) {
            _log.error("Failed to encrypt password.", e);
        }
        return "";
    }
