    public static String getHash(String userName, String pass) {
        String hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(userName.getBytes());
            hash = ISOUtil.hexString(md.digest(pass.getBytes())).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
        }
        return hash;
    }
