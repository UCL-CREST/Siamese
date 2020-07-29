    public static String SHA256(String source) {
        logger.info(source);
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(source.getBytes());
            byte[] bytes = digest.digest();
            result = EncodeUtils.hexEncode(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        logger.info(result);
        return result;
    }
