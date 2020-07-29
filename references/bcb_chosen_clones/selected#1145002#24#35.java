    private static byte[] calcMd5(String pass) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(pass.getBytes(), 0, pass.length());
            byte[] hash = digest.digest();
            return hash;
        } catch (NoSuchAlgorithmException e) {
            System.err.println("No MD5 algorithm found");
            System.exit(1);
        }
        return null;
    }
