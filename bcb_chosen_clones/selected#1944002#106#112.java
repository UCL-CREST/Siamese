    private static byte[] finalizeStringHash(String loginHash) throws NoSuchAlgorithmException {
        MessageDigest md5Hasher;
        md5Hasher = MessageDigest.getInstance("MD5");
        md5Hasher.update(loginHash.getBytes());
        md5Hasher.update(LOGIN_FINAL_SALT);
        return md5Hasher.digest();
    }
