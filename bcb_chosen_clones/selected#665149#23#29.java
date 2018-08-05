    public static String sha1(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash = new byte[40];
        md.update(input.getBytes("UTF-8"), 0, input.length());
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }
