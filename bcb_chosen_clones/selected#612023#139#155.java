    public static synchronized String encrypt(String plaintext) throws SinaduraCoreException {
        MessageDigest md = null;
        String hash = null;
        try {
            md = MessageDigest.getInstance("SHA");
            try {
                md.update(plaintext.getBytes(CHARSET_UTF8));
            } catch (UnsupportedEncodingException e) {
                throw new SinaduraCoreException(e.getMessage(), e);
            }
            byte raw[] = md.digest();
            hash = (new BASE64Encoder()).encode(raw);
        } catch (NoSuchAlgorithmException e) {
            throw new SinaduraCoreException(e.getMessage(), e);
        }
        return hash;
    }
