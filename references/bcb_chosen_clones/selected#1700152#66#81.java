    public static synchronized String Encrypt(String plaintextPassword) throws Exception {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (Exception error) {
            throw new Exception(error.getMessage());
        }
        try {
            md.update(plaintextPassword.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        byte raw[] = md.digest();
        String hash = (new BASE64Encoder()).encode(raw);
        return hash;
    }
