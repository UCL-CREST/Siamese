    public static String generate(String presentity, String eventPackage) {
        if (presentity == null || eventPackage == null) {
            return null;
        }
        String date = Long.toString(System.currentTimeMillis());
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.update(presentity.getBytes());
            md.update(eventPackage.getBytes());
            md.update(date.getBytes());
            byte[] digest = md.digest();
            return toHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
