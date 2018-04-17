    public static String calculateHA1(String username, byte[] password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(getBytes(username, ISO_8859_1));
            md.update((byte) ':');
            md.update(getBytes(DAAP_REALM, ISO_8859_1));
            md.update((byte) ':');
            md.update(password);
            return toHexString(md.digest());
        } catch (NoSuchAlgorithmException err) {
            throw new RuntimeException(err);
        }
    }
