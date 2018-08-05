    public static String encrypt(String password) throws NoSuchAlgorithmException {
        java.security.MessageDigest d = null;
        d = java.security.MessageDigest.getInstance("MD5");
        d.reset();
        d.update(password.getBytes());
        byte[] cr = d.digest();
        return getString(cr).toLowerCase();
    }
