    protected static void _s_encryptRsa_(PublicKey pky, InputStream ism, OutputStream osm, int intSizeFileInput, String strInstanceCipherAlgo) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IOException, IllegalBlockSizeException, BadPaddingException, java.security.NoSuchProviderException {
        Cipher cip = Cipher.getInstance(strInstanceCipherAlgo, "BC");
        cip.init(Cipher.ENCRYPT_MODE, pky);
        byte[] bytsBuffer = new byte[intSizeFileInput];
        int intBytesRead;
        while ((intBytesRead = ism.read(bytsBuffer)) != -1) {
        }
        byte[] bytsTarget = cip.doFinal(bytsBuffer);
        osm.write(bytsTarget);
        osm.close();
    }
