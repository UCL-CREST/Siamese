    private static String hashPass(String p) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash = new byte[40];
        md.update(p.getBytes("iso-8859-1"), 0, p.length());
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }
