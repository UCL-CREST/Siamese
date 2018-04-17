    public static final String hash(String data) {
        if (digest == null) {
            synchronized (StringUtils.class) {
                if (digest == null) {
                    try {
                        digest = MessageDigest.getInstance("MD5");
                    } catch (NoSuchAlgorithmException nsae) {
                    }
                }
            }
        }
        digest.update(data.getBytes());
        return toHex(digest.digest());
    }
