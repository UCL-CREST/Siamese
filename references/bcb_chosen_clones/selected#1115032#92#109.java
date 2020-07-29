    public static String generateMD5(final String data) {
        final StringBuilder hash = new StringBuilder(64);
        try {
            final MessageDigest sha1 = MessageDigest.getInstance("MD5");
            sha1.update(data.getBytes());
            final byte[] digest = sha1.digest();
            for (byte aDigest : digest) {
                String hex = Integer.toHexString(aDigest);
                if (hex.length() == 1) {
                    hex = "0" + hex;
                }
                hex = hex.substring(hex.length() - 2);
                hash.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
        }
        return hash.toString();
    }
