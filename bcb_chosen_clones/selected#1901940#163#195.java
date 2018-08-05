    public static void main(String[] args) {
        try {
            CurveParams curveParams = new CurveParams();
            InputStream inputStream = UTMAStrongEngineTest.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties");
            curveParams.load(inputStream);
            inputStream.close();
            inputStream = UTMAStrongEngineTest.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/elgamal_1024.params");
            ObjectInputStream ooi = new ObjectInputStream(inputStream);
            BigInteger g = (BigInteger) ooi.readObject();
            BigInteger p = (BigInteger) ooi.readObject();
            Integer l = (Integer) ooi.readObject();
            ooi.close();
            UTMAStrongParametersGenerator utmaStrongParametersGenerator = new UTMAStrongParametersGenerator();
            utmaStrongParametersGenerator.init(curveParams, new ElGamalParameters(p, g, l));
            UTMAStrongParameters utmaParameters = utmaStrongParametersGenerator.generateParameters();
            FileOutputStream fileOutputStream = new FileOutputStream("utmas.params");
            utmaStrongParametersGenerator.store(fileOutputStream, utmaParameters);
            fileOutputStream.flush();
            fileOutputStream.close();
            UTMAStrongKeyPairGenerator utmaStrongKeyPairGenerator = new UTMAStrongKeyPairGenerator();
            utmaStrongKeyPairGenerator.init(new UTMAStrongKeyGenerationParameters(new SecureRandom(), utmaParameters));
            AsymmetricCipherKeyPair keyPair = utmaStrongKeyPairGenerator.generateKeyPair();
            fileOutputStream = new FileOutputStream("utmas_keypair.params");
            utmaStrongKeyPairGenerator.store(fileOutputStream, keyPair);
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream = new FileInputStream("utmas_keypair.params");
            keyPair = utmaStrongKeyPairGenerator.load(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
