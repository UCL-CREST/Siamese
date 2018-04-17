    public static String getHash(String text) {
        if (text == null) return null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes());
            byte[] hashedTextBytes = md.digest();
            BigInteger hashedTextBigInteger = new BigInteger(1, hashedTextBytes);
            String hashedTextString = hashedTextBigInteger.toString(16);
            return hashedTextString;
        } catch (NoSuchAlgorithmException e) {
            LOG.warning(e.toString());
            return null;
        }
    }
