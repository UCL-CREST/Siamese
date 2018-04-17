    public String shortURL(String url) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
        digest.update(url.getBytes());
        return new String(digest.digest());
    }
