    public boolean verify(String digest, String password) throws NoSuchAlgorithmException {
        String alg = null;
        int size = 0;
        if (digest.regionMatches(true, 0, "{CRYPT}", 0, 7)) {
            digest = digest.substring(7);
            return UnixCrypt.matches(digest, password);
        } else if (digest.regionMatches(true, 0, "{SHA}", 0, 5)) {
            digest = digest.substring(5);
            alg = "SHA-1";
            size = 20;
        } else if (digest.regionMatches(true, 0, "{SSHA}", 0, 6)) {
            digest = digest.substring(6);
            alg = "SHA-1";
            size = 20;
        } else if (digest.regionMatches(true, 0, "{MD5}", 0, 5)) {
            digest = digest.substring(5);
            alg = "MD5";
            size = 16;
        } else if (digest.regionMatches(true, 0, "{SMD5}", 0, 6)) {
            digest = digest.substring(6);
            alg = "MD5";
            size = 16;
        }
        MessageDigest msgDigest = MessageDigest.getInstance(alg);
        byte[][] hs = split(Base64.decode(digest.toCharArray()), size);
        byte[] hash = hs[0];
        byte[] salt = hs[1];
        msgDigest.reset();
        msgDigest.update(password.getBytes());
        msgDigest.update(salt);
        byte[] pwhash = msgDigest.digest();
        return msgDigest.isEqual(hash, pwhash);
    }
