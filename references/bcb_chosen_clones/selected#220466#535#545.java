    private String calculatePassword(String string) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            md5.update(nonce.getBytes());
            md5.update(string.getBytes());
            return toHexString(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            error("MD5 digest is no supported !!!", "ERROR");
            return null;
        }
    }
