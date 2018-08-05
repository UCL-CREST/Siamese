    public static String hash(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5hash = new byte[32];
            md.update(value.getBytes("iso-8859-1"), 0, value.length());
            md5hash = md.digest();
            return Hex.encodeHexString(md5hash);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
