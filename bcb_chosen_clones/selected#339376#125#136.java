    public static String hash(String plaintext) {
        if (plaintext == null) {
            return "";
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA1");
            md.update(plaintext.getBytes("UTF-8"));
        } catch (Exception e) {
        }
        return new String(Base64.encodeBase64(md.digest()));
    }
