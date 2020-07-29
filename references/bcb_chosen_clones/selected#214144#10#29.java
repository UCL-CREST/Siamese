    public static String plainStringToMD5(String input) {
        if (input == null) {
            throw new NullPointerException("Input cannot be null");
        }
        MessageDigest md = null;
        byte[] byteHash = null;
        StringBuffer resultString = new StringBuffer();
        try {
            md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(input.getBytes());
            byteHash = md.digest();
            for (int i = 0; i < byteHash.length; i++) {
                resultString.append(Integer.toHexString(0xFF & byteHash[i]));
            }
            return (resultString.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
