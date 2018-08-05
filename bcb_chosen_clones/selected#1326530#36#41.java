    public static String digest(String value, String algorithm) throws Exception {
        MessageDigest algo = MessageDigest.getInstance(algorithm);
        algo.reset();
        algo.update(value.getBytes("UTF-8"));
        return StringTool.byteArrayToString(algo.digest());
    }
