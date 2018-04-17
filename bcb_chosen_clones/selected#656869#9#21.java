    public static synchronized String encrypt(String text) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes("UTF-8"));
            byte raw[] = md.digest();
            return new BASE64Encoder().encode(raw);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
