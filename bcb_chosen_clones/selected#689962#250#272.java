    private byte[] scramble411(String password, String seed) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
            byte[] passwordHashStage1 = md.digest(password.getBytes());
            md.reset();
            byte[] passwordHashStage2 = md.digest(passwordHashStage1);
            md.reset();
            md.update(seed.getBytes());
            md.update(passwordHashStage2);
            byte[] toBeXord = md.digest();
            int numToXor = toBeXord.length;
            for (int i = 0; i < numToXor; i++) {
                toBeXord[i] = (byte) (toBeXord[i] ^ passwordHashStage1[i]);
            }
            return toBeXord;
        } catch (NoSuchAlgorithmException e) {
            if (logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        return null;
    }
