    public void createPairKey() {
        try {
            KeyPairGenerator keygen = KeyPairGenerator.getInstance("DSA");
            SecureRandom random = new SecureRandom();
            random.setSeed(1000);
            keygen.initialize(512, random);
            KeyPair keys = keygen.generateKeyPair();
            PublicKey pubkey = keys.getPublic();
            PrivateKey prikey = keys.getPrivate();
            doObjToFile("mykeys.bat", new Object[] { prikey, pubkey });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
