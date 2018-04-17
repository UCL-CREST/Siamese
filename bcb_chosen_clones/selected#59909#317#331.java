    public static final synchronized String hash(String data) {
        if (digest == null) {
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException nsae) {
                log.error("Failed to load the MD5 MessageDigest. " + "Jive will be unable to function normally.", nsae);
            }
        }
        try {
            digest.update(data.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            log.error(e);
        }
        return encodeHex(digest.digest());
    }
