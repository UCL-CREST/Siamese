    public static byte[] getBytes(String s) throws Exception {
        java.security.MessageDigest d = null;
        d = java.security.MessageDigest.getInstance("SHA-1");
        d.reset();
        d.update(s.getBytes());
        return d.digest();
    }
