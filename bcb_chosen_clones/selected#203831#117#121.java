    public static String getStringDigest(String inputString) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(inputString.getBytes(), 0, inputString.length());
        return new BigInteger(1, md.digest()).toString(16);
    }
