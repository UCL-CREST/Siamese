    public static String getHash(String plaintext) {
        String hash = null;
        try {
            String text = plaintext;
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA-256");
                md.update(text.getBytes("UTF-8"));
                byte[] rawBytes = md.digest();
                hash = new BASE64Encoder().encode(rawBytes);
            } catch (NoSuchAlgorithmException e) {
            }
        } catch (IOException e) {
        }
        return hash;
    }
