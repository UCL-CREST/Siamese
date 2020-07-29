    public String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance(SHA1);
        md.update(text.getBytes(CHAR_SET), 0, text.length());
        byte[] mdbytes = md.digest();
        return byteToHex(mdbytes);
    }
