    public static String hash(String text) {
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("SHA-1");
            md.update(text.getBytes("UTF-8"), 0, text.length());
            byte[] md5hash = md.digest();
            return convertToHex(md5hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
