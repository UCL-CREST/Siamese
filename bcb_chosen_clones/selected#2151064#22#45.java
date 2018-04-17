    private static PublicKey generateKey() {
        try {
            String keyFileName = System.getProperty("java.io.tmpdir") + File.separator + "PUBLIC_KEY.properties";
            System.out.println("KEY FILE = " + keyFileName);
            File keyFile = new File(keyFileName);
            if (keyFile.exists()) {
                ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(keyFile));
                return (PublicKey) objIn.readObject();
            } else {
                KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
                SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
                keyGen.initialize(512, random);
                KeyPair pair = keyGen.generateKeyPair();
                PublicKey key = pair.getPublic();
                ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(keyFile));
                outStream.writeObject(key);
                outStream.close();
                return key;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
