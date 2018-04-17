    public static String getMD5(final String text) {
        if (null == text) return null;
        final MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        algorithm.reset();
        algorithm.update(text.getBytes());
        final byte[] digest = algorithm.digest();
        final StringBuffer hexString = new StringBuffer();
        for (byte b : digest) {
            String str = Integer.toHexString(0xFF & b);
            str = str.length() == 1 ? '0' + str : str;
            hexString.append(str);
        }
        return hexString.toString();
    }
