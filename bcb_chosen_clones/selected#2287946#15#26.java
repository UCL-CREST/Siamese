    public String hash(String password) {
        MessageDigest sha1Digest;
        try {
            sha1Digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw NestedException.wrap(e);
        }
        sha1Digest.update(password.getBytes());
        StringBuilder retval = new StringBuilder("sha1:");
        retval.append(new String(Base64.encodeBase64(sha1Digest.digest())));
        return retval.toString();
    }
