    public byte[] computeMD5(String plainText) throws GException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            throw new GException("The MD5 hash algorithm is not available.", ex);
        }
        try {
            md.update(plainText.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new GException("The UTF-8 encoding is not supported.", ex);
        }
        return md.digest();
    }
