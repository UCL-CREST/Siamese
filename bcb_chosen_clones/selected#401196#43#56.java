    public byte[] computeMD5(String plainText) throws VHException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            throw new VHException("The MD5 hash algorithm is not available.", ex);
        }
        try {
            md.update(plainText.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new VHException("The UTF-8 encoding is not supported.", ex);
        }
        return md.digest();
    }
