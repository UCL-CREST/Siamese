    public static void encrypt(EncryptionKeys encryptionKeys, File uploadedFile, File encryptedFile, String passphrase) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        SecureRandom random = new SecureRandom();
        String secretKeyType = "PBEWITHSHA256AND256BITAES-CBC-BC";
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(secretKeyType);
        PBEKeySpec eKeySpec = new PBEKeySpec(DigestUtils.md5Hex(passphrase).toCharArray(), encryptionKeys.getSaltBytes(), encryptionKeys.getIterationCount(), 1024);
        SecretKey eKey = keyFactory.generateSecret(eKeySpec);
        Cipher eCipher = Cipher.getInstance("AES/CTR/NOPADDING");
        IvParameterSpec eIvParameterSpec = new IvParameterSpec(encryptionKeys.getIvBytes());
        eCipher.init(Cipher.ENCRYPT_MODE, eKey, eIvParameterSpec, random);
        InputStream clearTextFis = new FileInputStream(uploadedFile);
        OutputStream cipherTextFos = new FileOutputStream(encryptedFile);
        cipherTextFos = new CipherOutputStream(cipherTextFos, eCipher);
        byte[] ebuf = new byte[4096];
        int enumRead = 0;
        while ((enumRead = clearTextFis.read(ebuf)) >= 0) {
            cipherTextFos.write(ebuf, 0, enumRead);
        }
        cipherTextFos.close();
    }
