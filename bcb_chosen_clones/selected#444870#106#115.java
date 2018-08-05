    private String md5(String input) {
        MessageDigest md5Digest;
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new UserException("could not get a md5 message digest", e);
        }
        md5Digest.update(input.getBytes());
        return new String(md5Digest.digest());
    }
