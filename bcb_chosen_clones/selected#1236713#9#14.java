    public static byte[] hash(String s) throws GeneralSecurityException {
        MessageDigest d = MessageDigest.getInstance("SHA-1");
        d.reset();
        d.update(s.getBytes());
        return d.digest();
    }
