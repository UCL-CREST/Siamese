    private byte[] md5Digest(String pPassword) {
        if (pPassword == null) {
            throw new NullPointerException("input null text for hashing");
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pPassword.getBytes());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Cannot find MD5 algorithm");
        }
    }
