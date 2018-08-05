    public static void generateKeyPair(String publicKeyFile, String privateKeyFile) throws IOException, NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keygen = KeyPairGenerator.getInstance("DSA", "SUN");
        keygen.initialize(512, new SecureRandom());
        KeyPair keys = keygen.generateKeyPair();
        PublicKey publicKey = keys.getPublic();
        PrivateKey privateKey = keys.getPrivate();
        byte[] bPublicKey = publicKey.getEncoded();
        FileOutputStream pubkeyfos = new FileOutputStream(publicKeyFile);
        pubkeyfos.write(bPublicKey);
        pubkeyfos.close();
        byte[] bPrivateKey = privateKey.getEncoded();
        FileOutputStream privkeyfos = new FileOutputStream(privateKeyFile);
        privkeyfos.write(bPrivateKey);
        privkeyfos.close();
    }
