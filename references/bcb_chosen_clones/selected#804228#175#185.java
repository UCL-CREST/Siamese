    static String getMD5Sum(String source) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(source.getBytes());
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm seems to not be supported. This is a requirement!");
        }
    }
