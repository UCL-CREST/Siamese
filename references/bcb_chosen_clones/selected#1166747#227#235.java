    private static String digest(String buffer) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(buffer.getBytes());
            return new String(encodeHex(md5.digest(key)));
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }
