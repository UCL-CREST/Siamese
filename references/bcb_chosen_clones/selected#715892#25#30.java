    static byte[] genDigest(String pad, byte[] passwd) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(DIGEST_ALGORITHM);
        digest.update(pad.getBytes());
        digest.update(passwd);
        return digest.digest();
    }
