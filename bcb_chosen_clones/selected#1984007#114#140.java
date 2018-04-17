    public static void createKeyPars(String dir) {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            String prvKeyFile = dir + "privateKey.scrpt";
            String pubKeyFile = dir + "publicKey.scrpt";
            FileOutputStream prvfos = new FileOutputStream(prvKeyFile);
            ObjectOutputStream prvoos = new ObjectOutputStream(prvfos);
            prvoos.writeObject(privateKey);
            prvoos.close();
            prvfos.close();
            FileOutputStream pubfos = new FileOutputStream(pubKeyFile);
            ObjectOutputStream puboos = new ObjectOutputStream(pubfos);
            puboos.writeObject(publicKey);
            puboos.close();
            pubfos.close();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
