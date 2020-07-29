    public byte[] generateKeyData() {
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        }
        try {
            try {
                random = SecureRandom.getInstance("SHA1PRNG");
                if (new File("/dev/urandom").exists()) {
                    byte[] salt = new byte[8192];
                    new FileInputStream("/dev/urandom").read(salt);
                    random.setSeed(salt);
                }
            } catch (Exception e) {
                LOG.fatal("Exception", e);
                random = new SecureRandom();
            }
            KeyPairGenerator keyGenerator;
            keyGenerator = KeyPairGenerator.getInstance("RSA", "BC");
            keyGenerator.initialize(1024, random);
            KeyPair keyPair = keyGenerator.generateKeyPair();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            publicKey = (RSAPublicKey) keyPair.getPublic();
            oos.writeObject(publicKey.getModulus());
            oos.writeObject(publicKey.getPublicExponent());
            privateKey = (RSAPrivateKey) keyPair.getPrivate();
            oos.writeObject(privateKey.getModulus());
            oos.writeObject(privateKey.getPrivateExponent());
            oos.flush();
            oos.close();
            return bos.toByteArray();
        } catch (Exception e) {
            LOG.error("Exception", e);
            return null;
        }
    }
