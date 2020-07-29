    public byte[] md5(String clearText) {
        MessageDigest md;
        byte[] digest;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(clearText.getBytes());
            digest = md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new UnsupportedOperationException(e.toString());
        }
        return digest;
    }
