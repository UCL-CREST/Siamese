    public static synchronized String hash(String data) {
        if (digest == null) {
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException nsae) {
            }
        }
        try {
            digest.update(data.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
        }
        return encodeHex(digest.digest());
    }
