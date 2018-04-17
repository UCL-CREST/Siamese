    public static final String getHash(int iterationNb, String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(salt.getBytes("UTF-8"));
            byte[] input = digest.digest(password.getBytes("UTF-8"));
            for (int i = 0; i < iterationNb; i++) {
                digest.reset();
                input = digest.digest(input);
            }
            String hashedValue = encoder.encode(input);
            LOG.finer("Creating hash '" + hashedValue + "' with iterationNb '" + iterationNb + "' and password '" + password + "' and salt '" + salt + "'!!");
            return hashedValue;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Problem in the getHash method.", ex);
        }
    }
