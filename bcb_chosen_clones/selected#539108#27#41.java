    public synchronized String encrypt(String plaintext) {
        MessageDigest md = null;
        String hash = null;
        try {
            md = MessageDigest.getInstance("SHA");
            md.update(plaintext.getBytes("UTF-8"));
            byte raw[] = md.digest();
            hash = (new BASE64Encoder()).encode(raw);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return hash;
    }
