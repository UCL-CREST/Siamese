    private static String getMD5(String phrase) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(phrase.getBytes());
            return asHexString(md.digest());
        } catch (Exception e) {
        }
        return "";
    }
