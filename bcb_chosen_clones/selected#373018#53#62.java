    private static void generateKeyPair() throws Exception {
        SecureRandom sr = new SecureRandom();
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
        kpg.initialize(KEYSIZE, sr);
        KeyPair kp = kpg.generateKeyPair();
        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();
        writePublicKey(PUBLIC_KEY_FILE);
        writePrivateKey(PRIVATE_KEY_FILE);
    }
