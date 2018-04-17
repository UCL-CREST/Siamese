    public static String calculateSHA1(String data, String key) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        data += key;
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash = new byte[40];
        md.update(data.getBytes("iso-8859-1"), 0, data.length());
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }
