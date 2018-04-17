    public static String str2md5(String str) {
        try {
            MessageDigest alga = MessageDigest.getInstance(MESSAGE_DIGEST_TYPE);
            alga.update(str.getBytes());
            byte[] digesta = alga.digest();
            return byte2hex(digesta);
        } catch (NoSuchAlgorithmException ex) {
            return str;
        }
    }
