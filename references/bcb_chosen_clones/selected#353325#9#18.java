    public static String SHA1(String password) throws BusinessException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(password.getBytes());
            BigInteger hash = new BigInteger(1, digest.digest());
            return hash.toString(16);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new BusinessException();
        }
    }
