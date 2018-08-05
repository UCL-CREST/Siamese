    public synchronized byte[] encrypt(byte[] what, String publicKey) {
        byte[] aesKeyBytes = null;
        Cipher cipherAES = null;
        Cipher cipherRSA = null;
        int cipherRSAinSize = 0;
        int cipherRSAoutSize = 0;
        aesKeyBytes = generateAESSessionKey();
        if (aesKeyBytes == null) {
            return null;
        }
        cipherAES = buildCipherAES(Cipher.ENCRYPT_MODE, aesKeyBytes);
        ;
        if (cipherAES == null) {
            return null;
        }
        try {
            StringTokenizer keycutter = new StringTokenizer(publicKey, ":");
            BigInteger Exponent = new BigInteger(Base64.decode(keycutter.nextToken()));
            BigInteger Modulus = new BigInteger(Base64.decode(keycutter.nextToken()));
            RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(Modulus, Exponent);
            KeyFactory fact = KeyFactory.getInstance("RSA", "BC");
            PublicKey pubKey = fact.generatePublic(pubKeySpec);
            cipherRSA = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
            cipherRSA.init(Cipher.ENCRYPT_MODE, pubKey);
            cipherRSAinSize = cipherRSA.getBlockSize();
            cipherRSAoutSize = cipherRSA.getOutputSize(cipherRSAinSize);
            if (cipherRSAinSize != 117 || cipherRSAoutSize != 128) {
                throw new Exception("block size invalid, inSize=" + cipherRSAinSize + "; outSize=" + cipherRSAoutSize);
            }
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Error in encrypt, RSA preparation", t);
            return null;
        }
        byte rsaEncData[] = null;
        try {
            byte[] rsaInpData = new byte[cipherRSAinSize];
            byte[] randomBytes = new byte[cipherRSAinSize - aesKeyBytes.length];
            getSecureRandom().nextBytes(randomBytes);
            System.arraycopy(aesKeyBytes, 0, rsaInpData, 0, aesKeyBytes.length);
            System.arraycopy(randomBytes, 0, rsaInpData, aesKeyBytes.length, randomBytes.length);
            rsaEncData = cipherRSA.doFinal(rsaInpData, 0, rsaInpData.length);
            if (rsaEncData.length != cipherRSAoutSize) {
                throw new Exception("RSA out block size invalid: " + rsaEncData.length);
            }
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Error in encrypt, RSA encryption", t);
            return null;
        }
        ByteArrayOutputStream plainOut = new ByteArrayOutputStream(what.length + (what.length / 10) + rsaEncData.length);
        try {
            plainOut.write(rsaEncData);
            CipherOutputStream cOut = new CipherOutputStream(plainOut, cipherAES);
            cOut.write(what);
            cOut.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error in encrypt, AES encryption", e);
            return null;
        }
        return plainOut.toByteArray();
    }
