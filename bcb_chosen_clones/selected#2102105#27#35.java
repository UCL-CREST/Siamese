    public static String getSHA1Digest(String inputStr) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = null;
        byte[] sha1hash = null;
        md = MessageDigest.getInstance("SHA");
        sha1hash = new byte[40];
        md.update(inputStr.getBytes("iso-8859-1"), 0, inputStr.length());
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }
