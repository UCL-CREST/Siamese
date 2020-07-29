    public static String digest(String ha1, String ha2, String nonce) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(getBytes(ha1, ISO_8859_1));
            md.update((byte) ':');
            md.update(getBytes(nonce, ISO_8859_1));
            md.update((byte) ':');
            md.update(getBytes(ha2, ISO_8859_1));
            return toHexString(md.digest());
        } catch (NoSuchAlgorithmException err) {
            throw new RuntimeException(err);
        }
    }
