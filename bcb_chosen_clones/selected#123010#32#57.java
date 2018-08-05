    public String encrypt(String password) {
        if (password.length() == 40) {
            return password;
        }
        if (salt != null) {
            password = password + salt;
        }
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        messageDigest.reset();
        messageDigest.update(password.getBytes());
        final byte[] bytes = messageDigest.digest();
        String encrypted = new BigInteger(1, bytes).toString(16);
        if (encrypted.length() < 40) {
            final StringBuilder builder = new StringBuilder(encrypted);
            while (builder.length() < 40) {
                builder.insert(0, '0');
            }
            encrypted = builder.toString();
        }
        return encrypted;
    }
