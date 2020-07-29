    public Console() {
        try {
            File pubKeyFile = new File("publicKey");
            File privKeyFile = new File("privateKey");
            byte[] pubByteArray;
            byte[] privByteArray;
            if (pubKeyFile.exists() && privKeyFile.exists()) {
                FileInputStream publicKeyStream = new FileInputStream(pubKeyFile);
                FileInputStream privateKeyStream = new FileInputStream(privKeyFile);
                pubByteArray = new byte[(int) pubKeyFile.length()];
                privByteArray = new byte[(int) privKeyFile.length()];
                publicKeyStream.read(pubByteArray);
                privateKeyStream.read(privByteArray);
            } else {
                System.out.println("Generating Key Pair...");
                KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
                SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
                keyGen.initialize(1024, random);
                KeyPair pair = keyGen.generateKeyPair();
                PrivateKey priv = pair.getPrivate();
                PublicKey pub = pair.getPublic();
                pubByteArray = pub.getEncoded();
                FileOutputStream publicKeyStream = new FileOutputStream(pubKeyFile);
                publicKeyStream.write(pubByteArray);
                publicKeyStream.close();
                privByteArray = priv.getEncoded();
                FileOutputStream privateKeyStream = new FileOutputStream(privKeyFile);
                privateKeyStream.write(privByteArray);
                privateKeyStream.close();
                System.out.println("Generated Key Pair...");
            }
            new Qore(pubByteArray, privByteArray);
        } catch (Exception e) {
            System.err.println("Caught exception " + e);
        }
    }
