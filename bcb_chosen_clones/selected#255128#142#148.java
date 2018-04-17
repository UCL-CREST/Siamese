    public static byte[] encrypt(String x) throws NoSuchAlgorithmException {
        MessageDigest d = null;
        d = MessageDigest.getInstance("SHA-1");
        d.reset();
        d.update(x.getBytes());
        return d.digest();
    }
