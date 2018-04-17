    public static String encrypt(String plaintext) throws EncryptionException {
        if (plaintext == null) {
            throw new EncryptionException();
        }
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(plaintext.getBytes("UTF-8"));
            return Base64.encodeBytes(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionException(e);
        } catch (UnsupportedEncodingException e) {
            throw new EncryptionException(e);
        }
    }
