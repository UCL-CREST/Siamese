    public static String MD5Digest(String source) {
        MessageDigest digest;
        try {
            digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(source.getBytes("UTF8"));
            byte[] hash = digest.digest();
            String strHash = byteArrayToHexString(hash);
            return strHash;
        } catch (NoSuchAlgorithmException e) {
            String msg = "%s: %s";
            msg = String.format(msg, e.getClass().getName(), e.getMessage());
            logger.error(msg);
            return null;
        } catch (UnsupportedEncodingException e) {
            String msg = "%s: %s";
            msg = String.format(msg, e.getClass().getName(), e.getMessage());
            logger.error(msg);
            return null;
        }
    }
