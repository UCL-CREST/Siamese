    public static final String enCode(String algorithm, String string) {
        MessageDigest md;
        String result = "";
        try {
            md = MessageDigest.getInstance(algorithm);
            md.update(string.getBytes());
            result = binaryToString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }
