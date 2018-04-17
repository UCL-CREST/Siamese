    public static void encrypt(EncryptionKeys encryptionKeys, File uploadedFile, File encryptedFile) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        SecretKeySpec encryptionKey = new SecretKeySpec(encryptionKeys.getKeyBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);
        InputStream clearTextFis = new FileInputStream(uploadedFile);
        OutputStream cipherTextFos = new FileOutputStream(encryptedFile);
        cipherTextFos = new CipherOutputStream(cipherTextFos, cipher);
        byte[] buf = new byte[4096];
        int numRead = 0;
        while ((numRead = clearTextFis.read(buf)) >= 0) {
            cipherTextFos.write(buf, 0, numRead);
        }
        cipherTextFos.close();
    }
