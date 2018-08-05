    public static String digest(String algorithm, String text) {
        MessageDigest mDigest = null;
        try {
            mDigest = MessageDigest.getInstance(algorithm);
            mDigest.update(text.getBytes(ENCODING));
        } catch (NoSuchAlgorithmException nsae) {
            Logger.error(Encryptor.class, nsae.getMessage(), nsae);
        } catch (UnsupportedEncodingException uee) {
            Logger.error(Encryptor.class, uee.getMessage(), uee);
        }
        byte raw[] = mDigest.digest();
        return (new BASE64Encoder()).encode(raw);
    }
