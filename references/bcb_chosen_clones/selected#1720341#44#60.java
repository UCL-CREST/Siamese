    public final String encrypt(final String plaintext, final String salt) {
        if (plaintext == null) {
            throw new NullPointerException();
        }
        if (salt == null) {
            throw new NullPointerException();
        }
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA");
            md.update((plaintext + salt).getBytes("UTF-8"));
            return new BASE64Encoder().encode(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionException(e);
        } catch (UnsupportedEncodingException e) {
            throw new EncryptionException(e);
        }
    }
