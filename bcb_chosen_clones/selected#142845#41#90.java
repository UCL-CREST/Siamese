    public String encrypt(final char[] password, final String algorithm, final byte[] salt) {
        final StringBuffer buffer = new StringBuffer();
        MessageDigest digest = null;
        int size = 0;
        if ("SHA".equalsIgnoreCase(algorithm) || "SSHA".equalsIgnoreCase(algorithm)) {
            size = 20;
            if ((salt != null) && (salt.length > 0)) {
                buffer.append("{SSHA}");
            } else {
                buffer.append("{SHA}");
            }
            try {
                digest = MessageDigest.getInstance("SHA-1");
            } catch (final NoSuchAlgorithmException e) {
                throw new IllegalStateException("Invalid algorithm");
            }
        } else if ("MD5".equalsIgnoreCase(algorithm) || "SMD5".equalsIgnoreCase(algorithm)) {
            size = 16;
            if ((salt != null) && (salt.length > 0)) {
                buffer.append("{SMD5}");
            } else {
                buffer.append("{MD5}");
            }
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (final NoSuchAlgorithmException e) {
                throw new IllegalStateException("Invalid algorithm");
            }
        } else {
            throw new UnsupportedOperationException("Not implemented");
        }
        int outSize = size;
        digest.reset();
        try {
            digest.update(new String(password).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 Unsupported");
        }
        if ((salt != null) && (salt.length > 0)) {
            digest.update(salt);
            outSize += salt.length;
        }
        final byte[] out = new byte[outSize];
        System.arraycopy(digest.digest(), 0, out, 0, size);
        if ((salt != null) && (salt.length > 0)) {
            System.arraycopy(salt, 0, out, size, salt.length);
        }
        buffer.append(new String(Base64.encodeBase64(out)));
        return buffer.toString();
    }
