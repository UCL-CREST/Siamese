    @Override
    public void generateKeyPair(File publicKeyFile, File privateKeyFile) throws FileNotFoundException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        SecureRandom random = new SecureRandom();
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
        generator.initialize(2048, random);
        KeyPair pair = generator.generateKeyPair();
        Key pubKey = pair.getPublic();
        PEMWriter pubWriter = new PEMWriter(new FileWriter(publicKeyFile));
        pubWriter.writeObject(pubKey);
        pubWriter.close();
        PEMWriter privWriter = new PEMWriter(new FileWriter(privateKeyFile));
        privWriter.writeObject(pair);
        privWriter.close();
    }
