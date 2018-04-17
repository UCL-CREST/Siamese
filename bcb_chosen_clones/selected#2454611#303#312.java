    static String encrypt(String plaintext) {
        MessageDigest d = null;
        try {
            d = MessageDigest.getInstance("SHA-1");
            d.update(plaintext.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(Base64.encodeBase64(d.digest()));
    }
