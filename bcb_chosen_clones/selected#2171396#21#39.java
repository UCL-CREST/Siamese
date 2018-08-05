    public static String hashNative(String password, String algorithm, String prefixKey, boolean useDefaultEncoding) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            if (useDefaultEncoding) {
                digest.update(password.getBytes());
            } else {
                for (char c : password.toCharArray()) {
                    digest.update((byte) (c >> 8));
                    digest.update((byte) c);
                }
            }
            byte[] digestedPassword = digest.digest();
            BASE64Encoder encoder = new BASE64Encoder();
            String encodedDigested = encoder.encode(digestedPassword);
            return prefixKey + encodedDigested;
        } catch (NoSuchAlgorithmException ne) {
            return password;
        }
    }
