    public static String convertStringToMD5(String toEnc) {
        try {
            MessageDigest mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(toEnc.getBytes(), 0, toEnc.length());
            return new BigInteger(1, mdEnc.digest()).toString(16);
        } catch (Exception e) {
            return null;
        }
    }
