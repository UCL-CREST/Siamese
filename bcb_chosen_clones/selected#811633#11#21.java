    private static String makeMD5(String str) {
        byte[] bytes = new byte[32];
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes("iso-8859-1"), 0, str.length());
            bytes = md.digest();
        } catch (Exception e) {
            return null;
        }
        return convertToHex(bytes);
    }
