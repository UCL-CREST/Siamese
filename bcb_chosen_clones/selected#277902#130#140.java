    @Digester(forField = "password")
    public static String encriptPassword(String passwd) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(passwd.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            return hash.toString(16);
        } catch (Exception e) {
            return null;
        }
    }
