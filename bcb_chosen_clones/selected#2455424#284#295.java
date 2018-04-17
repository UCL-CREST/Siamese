    private String encode(String plaintext) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(plaintext.getBytes("UTF-8"));
            byte raw[] = md.digest();
            return (new BASE64Encoder()).encode(raw);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Error encoding: " + e);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Error encoding: " + e);
        }
    }
