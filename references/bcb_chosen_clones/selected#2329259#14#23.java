    public static String sha1Hash(String input) {
        try {
            MessageDigest sha1Digest = MessageDigest.getInstance("SHA-1");
            sha1Digest.update(input.getBytes());
            return byteArrayToString(sha1Digest.digest());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }
