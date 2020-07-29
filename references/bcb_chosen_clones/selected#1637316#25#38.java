    public String getCipherString(String source) throws CadenaNoCifradaException {
        String encryptedSource = null;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
            byte[] sha1hash = new byte[40];
            md.update(source.getBytes(encoding), 0, source.length());
            sha1hash = md.digest();
            encryptedSource = convertToHex(sha1hash);
        } catch (Exception e) {
            throw new CadenaNoCifradaException(e);
        }
        return encryptedSource;
    }
