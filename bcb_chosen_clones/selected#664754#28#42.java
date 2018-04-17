    protected synchronized String encryptThis(String seed, String text) throws EncryptionException {
        String encryptedValue = null;
        String textToEncrypt = text;
        if (seed != null) {
            textToEncrypt = seed.toLowerCase() + text;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(textToEncrypt.getBytes("UTF-8"));
            encryptedValue = (new BASE64Encoder()).encode(md.digest());
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
        return encryptedValue;
    }
