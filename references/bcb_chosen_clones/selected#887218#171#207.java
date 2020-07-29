    public static boolean verifyPassword(String digest, String password) {
        String alg = null;
        int size = 0;
        if (digest.regionMatches(true, 0, "{SHA}", 0, 5)) {
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
        try {
            MessageDigest sha = MessageDigest.getInstance(alg);
            if (sha == null) {
                return false;
            }
            byte[][] hs = split(Base64.decode(digest), size);
            byte[] hash = hs[0];
            byte[] salt = hs[1];
            sha.reset();
            sha.update(password.getBytes());
            sha.update(salt);
            byte[] pwhash = sha.digest();
            return MessageDigest.isEqual(hash, pwhash);
        } catch (NoSuchAlgorithmException nsae) {
            throw new RuntimeException("failed to find " + "algorithm for password hashing.", nsae);
        }
    }
