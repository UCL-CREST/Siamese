    public static String hashJopl(String password, String algorithm, String prefixKey, boolean useDefaultEncoding) {
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
            String encodedDigestedStr = encoder.encode(digestedPassword);
            return prefixKey + encodedDigestedStr;
        } catch (NoSuchAlgorithmException ne) {
            return password;
        }
    }
