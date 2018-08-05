    public static String genetateSHA256(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes("UTF-8"));
        byte[] passWd = md.digest();
        String hex = toHex(passWd);
        return hex;
    }
