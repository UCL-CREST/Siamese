    public static synchronized String hash(String data) {
        if (digest == null) {
            try {
                digest = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException nsae) {
                System.err.println("Failed to load the SHA-1 MessageDigest. " + "Jive will be unable to function normally.");
            }
        }
        try {
            digest.update(data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.err.println(e);
        }
        return encodeHex(digest.digest());
    }
