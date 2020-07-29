    protected static void _s_encrypt_(SecretKey sky, InputStream ism, OutputStream osm, String strInstanceCipher) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IOException {
        Cipher cip = Cipher.getInstance(strInstanceCipher);
        cip.init(Cipher.ENCRYPT_MODE, sky);
        CipherOutputStream cos = new CipherOutputStream(osm, cip);
        byte[] bytsBuffer = new byte[2048];
        int intBytesRead;
        while ((intBytesRead = ism.read(bytsBuffer)) != -1) {
            cos.write(bytsBuffer, 0, intBytesRead);
        }
        cos.close();
        java.util.Arrays.fill(bytsBuffer, (byte) 0);
    }
