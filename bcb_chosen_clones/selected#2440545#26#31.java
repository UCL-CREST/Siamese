    public synchronized String encrypt(final String pPassword) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(pPassword.getBytes("UTF-8"));
        final byte raw[] = md.digest();
        return BASE64Encoder.encodeBuffer(raw);
    }
