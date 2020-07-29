    public static String calculateHA2(String uri) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(getBytes("GET", ISO_8859_1));
            md.update((byte) ':');
            md.update(getBytes(uri, ISO_8859_1));
            return toHexString(md.digest());
        } catch (NoSuchAlgorithmException err) {
            throw new RuntimeException(err);
        }
    }
