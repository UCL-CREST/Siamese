    private static byte[] baseHash(String name, String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(name.toLowerCase().getBytes());
            digest.update(password.getBytes());
            return digest.digest();
        } catch (NoSuchAlgorithmException ex) {
            d("MD5 algorithm not found!");
            throw new RuntimeException("MD5 algorithm not found! Unable to authenticate");
        }
    }
