    public synchronized String encrypt(String plaintext) {
        if (plaintext == null || plaintext.equals("")) {
            return plaintext;
        }
        String hash = null;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
        try {
            md.update(plaintext.getBytes("UTF-8"));
            byte raw[] = md.digest();
            hash = Base64.encodeBase64String(raw).replaceAll("\r\n", "");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
        return hash;
    }
