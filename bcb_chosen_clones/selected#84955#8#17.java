    public String generateToken(String code) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(code.getBytes());
            byte[] bytes = md.digest();
            return toHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA1 missing");
        }
    }
