    public static String MD5(String source) {
        logger.info(source);
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(source.getBytes());
            byte[] bytes = digest.digest();
            result = EncodeUtils.hexEncode(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        logger.info(result);
        return result;
    }
