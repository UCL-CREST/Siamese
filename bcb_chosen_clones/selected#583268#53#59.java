    public static byte[] encode(String origin, String algorithm) throws NoSuchAlgorithmException {
        String resultStr = null;
        resultStr = new String(origin);
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(resultStr.getBytes());
        return md.digest();
    }
