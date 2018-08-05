    public String getHash(final String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] md5hash;
        digest.update(password.getBytes("utf-8"), 0, password.length());
        md5hash = digest.digest();
        return convertToHex(md5hash);
    }
