    public static String MD5(String str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            md5.update(str.getBytes(), 0, str.length());
            String sig = new BigInteger(1, md5.digest()).toString();
            return sig;
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Can not use md5 algorithm");
        }
        return null;
    }
