    public static String md5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes());
            return convertToHex(md.digest());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
