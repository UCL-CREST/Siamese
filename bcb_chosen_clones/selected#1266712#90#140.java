    public static boolean verify(String password, String encryptedPassword) {
        MessageDigest digest = null;
        int size = 0;
        String base64 = null;
        if (encryptedPassword.regionMatches(true, 0, "{CRYPT}", 0, 7)) {
            throw new InternalError("Not implemented");
        } else if (encryptedPassword.regionMatches(true, 0, "{SHA}", 0, 5)) {
            size = 20;
            base64 = encryptedPassword.substring(5);
            try {
                digest = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                throw new InternalError("Invalid algorithm");
            }
        } else if (encryptedPassword.regionMatches(true, 0, "{SSHA}", 0, 6)) {
            size = 20;
            base64 = encryptedPassword.substring(6);
            try {
                digest = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                throw new InternalError("Invalid algorithm");
            }
        } else if (encryptedPassword.regionMatches(true, 0, "{MD5}", 0, 5)) {
            size = 16;
            base64 = encryptedPassword.substring(5);
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new InternalError("Invalid algorithm");
            }
        } else if (encryptedPassword.regionMatches(true, 0, "{SMD5}", 0, 6)) {
            size = 16;
            base64 = encryptedPassword.substring(6);
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new InternalError("Invalid algorithm");
            }
        } else {
            return false;
        }
        byte[] data = Base64.decode(base64.toCharArray());
        byte[] orig = new byte[size];
        System.arraycopy(data, 0, orig, 0, size);
        digest.reset();
        digest.update(password.getBytes());
        if (data.length > size) {
            digest.update(data, size, data.length - size);
        }
        return MessageDigest.isEqual(digest.digest(), orig);
    }
