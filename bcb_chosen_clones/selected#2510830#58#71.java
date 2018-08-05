    public static String encodePassword(String password, byte[] seed) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (seed == null) {
            seed = new byte[12];
            secureRandom.nextBytes(seed);
        }
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(seed);
        md.update(password.getBytes("UTF8"));
        byte[] digest = md.digest();
        byte[] storedPassword = new byte[digest.length + 12];
        System.arraycopy(seed, 0, storedPassword, 0, 12);
        System.arraycopy(digest, 0, storedPassword, 12, digest.length);
        return new sun.misc.BASE64Encoder().encode(storedPassword);
    }
