    public static final String getMD5Hash(String data, String key) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data.getBytes());
        return generateHash(md5.digest(key.getBytes()));
    }
