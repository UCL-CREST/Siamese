    public static String encrypt(String message) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(message.getBytes());
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(digest.digest());
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return null;
        }
    }
