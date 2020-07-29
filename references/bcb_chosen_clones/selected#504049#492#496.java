    private byte[] MD5Digest(String input) throws GeneralSecurityException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes());
        return md.digest();
    }
