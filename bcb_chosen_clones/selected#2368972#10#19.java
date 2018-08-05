    public static String getMd5Digest(String pInput) {
        try {
            MessageDigest lDigest = MessageDigest.getInstance("MD5");
            lDigest.update(pInput.getBytes());
            BigInteger lHashInt = new BigInteger(1, lDigest.digest());
            return String.format("%1$032x", lHashInt).toLowerCase();
        } catch (NoSuchAlgorithmException lException) {
            throw new RuntimeException(lException);
        }
    }
