    public static String md5hash(String input) {
        try {
            MessageDigest sha1Digest = MessageDigest.getInstance("MD5");
            sha1Digest.update(input.getBytes());
            return byteArrayToString(sha1Digest.digest());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }
