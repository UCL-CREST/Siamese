    private static String hashToMD5(String sig) {
        try {
            MessageDigest lDigest = MessageDigest.getInstance("MD5");
            lDigest.update(sig.getBytes());
            BigInteger lHashInt = new BigInteger(1, lDigest.digest());
            return String.format("%1$032X", lHashInt).toLowerCase();
        } catch (NoSuchAlgorithmException lException) {
            throw new RuntimeException(lException);
        }
    }
