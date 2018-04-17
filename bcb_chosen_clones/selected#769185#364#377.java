    private static byte[] encrypt(InputStream is, PublicKey pk) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        Cipher cip = Cipher.getInstance("RSA");
        cip.init(Cipher.ENCRYPT_MODE, pk);
        RSAPublicKey rpk = (RSAPublicKey) pk;
        int buLen = rpk.getModulus().bitLength() / 8 - 11;
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        byte data[] = new byte[buLen];
        while (is.available() > 0) {
            int size = is.read(data);
            bo.write(cip.doFinal(data, 0, size));
        }
        bo.close();
        return bo.toByteArray();
    }
