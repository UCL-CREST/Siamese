    public static void mEncrypt(InputStream pIn, OutputStream pOut, SecretKey pKey) throws Exception {
        Cipher xCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
        xCipher.init(Cipher.ENCRYPT_MODE, pKey, paramSpec);
        byte[] buf = new byte[1024];
        pOut = new CipherOutputStream(pOut, xCipher);
        int numRead = 0;
        while ((numRead = pIn.read(buf)) >= 0) {
            pOut.write(buf, 0, numRead);
        }
        pOut.close();
    }
