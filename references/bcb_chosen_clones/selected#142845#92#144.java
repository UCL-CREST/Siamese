    public boolean verify(final char[] password, final String encryptedPassword) {
        MessageDigest digest = null;
        int size = 0;
        String base64 = null;
        if (encryptedPassword.regionMatches(true, 0, "{SHA}", 0, 5)) {
            size = 20;
            base64 = encryptedPassword.substring(5);
            try {
                digest = MessageDigest.getInstance("SHA-1");
            } catch (final NoSuchAlgorithmException e) {
                throw new IllegalStateException("Invalid algorithm");
            }
        } else if (encryptedPassword.regionMatches(true, 0, "{SSHA}", 0, 6)) {
            size = 20;
            base64 = encryptedPassword.substring(6);
            try {
                digest = MessageDigest.getInstance("SHA-1");
            } catch (final NoSuchAlgorithmException e) {
                throw new IllegalStateException("Invalid algorithm");
            }
        } else if (encryptedPassword.regionMatches(true, 0, "{MD5}", 0, 5)) {
            size = 16;
            base64 = encryptedPassword.substring(5);
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (final NoSuchAlgorithmException e) {
                throw new IllegalStateException("Invalid algorithm");
            }
        } else if (encryptedPassword.regionMatches(true, 0, "{SMD5}", 0, 6)) {
            size = 16;
            base64 = encryptedPassword.substring(6);
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (final NoSuchAlgorithmException e) {
                throw new IllegalStateException("Invalid algorithm");
            }
        } else {
            return false;
        }
        try {
            final byte[] data = Base64.decodeBase64(base64.getBytes("UTF-8"));
            final byte[] orig = new byte[size];
            System.arraycopy(data, 0, orig, 0, size);
            digest.reset();
            digest.update(new String(password).getBytes("UTF-8"));
            if (data.length > size) {
                digest.update(data, size, data.length - size);
            }
            return MessageDigest.isEqual(digest.digest(), orig);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 Unsupported");
        }
    }
