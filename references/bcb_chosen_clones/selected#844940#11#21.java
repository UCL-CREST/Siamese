    public static byte[] hash(String plainTextValue) {
        MessageDigest msgDigest;
        try {
            msgDigest = MessageDigest.getInstance("MD5");
            msgDigest.update(plainTextValue.getBytes("UTF-8"));
            byte[] digest = msgDigest.digest();
            return digest;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
