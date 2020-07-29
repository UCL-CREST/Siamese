    public byte[] scramblePassword(String password, String seed) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] stage1 = md.digest(password.getBytes());
        md.reset();
        byte[] stage2 = md.digest(stage1);
        md.reset();
        md.update(seed.getBytes());
        md.update(stage2);
        byte[] result = md.digest();
        for (int i = 0; i < result.length; i++) {
            result[i] ^= stage1[i];
        }
        return result;
    }
