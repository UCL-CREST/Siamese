    public static String createHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            return toHexString(digest);
        } catch (NoSuchAlgorithmException nsae) {
            System.out.println(nsae.getMessage());
        }
        return "";
    }
