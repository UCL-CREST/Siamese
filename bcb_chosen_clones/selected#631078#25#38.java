    public static String digest(String algorithm, String text) {
        MessageDigest mDigest = null;
        try {
            mDigest = MessageDigest.getInstance(algorithm);
            mDigest.update(text.getBytes(ENCODING));
        } catch (NoSuchAlgorithmException nsae) {
            _log.error(nsae, nsae);
        } catch (UnsupportedEncodingException uee) {
            _log.error(uee, uee);
        }
        byte[] raw = mDigest.digest();
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(raw);
    }
