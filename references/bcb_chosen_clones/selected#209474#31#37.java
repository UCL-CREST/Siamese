    public static byte[] createPasswordDigest(String password, byte[] salt) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(salt);
        md.update(password.getBytes("UTF8"));
        byte[] digest = md.digest();
        return digest;
    }
