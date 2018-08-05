    public static String md5(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            return prepad(output, 32, '0');
        } catch (NoSuchAlgorithmException e) {
            System.err.println("No MD5 algorithm. we are sunk.");
            return s;
        }
    }
