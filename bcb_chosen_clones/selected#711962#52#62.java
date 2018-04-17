    public String getHash(String type, String text, String salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance(type);
        byte[] hash = new byte[md.getDigestLength()];
        if (!salt.isEmpty()) {
            md.update(salt.getBytes("iso-8859-1"), 0, salt.length());
        }
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        hash = md.digest();
        return convertToHex(hash);
    }
