    public void createSecretKey(String senha) throws Exception {
        PublicKey pk = getPublicKeyFromFile(new File(config.certificatesDir + "/store/truststore.jks"), "direto", new String(Base64Utils.decode(pwd.getBytes())));
        rsa.init(Cipher.ENCRYPT_MODE, pk);
        CipherOutputStream cos = new CipherOutputStream(new FileOutputStream(config.certificatesDir + "/AES/direto.pk"), rsa);
        cos.write(senha.getBytes());
        cos.close();
    }
