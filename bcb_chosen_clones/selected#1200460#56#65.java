    public static String hash(String plainTextPwd) {
        MessageDigest hashAlgo;
        try {
            hashAlgo = java.security.MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new QwickException(e);
        }
        hashAlgo.update(plainTextPwd.getBytes());
        return new String(hashAlgo.digest());
    }
