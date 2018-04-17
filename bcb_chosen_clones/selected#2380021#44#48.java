    byte[] hashPassword(String s) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(s.getBytes("UTF-16LE"));
        return md.digest();
    }
