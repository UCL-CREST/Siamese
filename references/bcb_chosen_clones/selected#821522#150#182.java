    public static void main(String[] args) throws Exception {
        File file = new File("test.html");
        FileInputStream in = new FileInputStream(file);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] tmpbuf = new byte[1024];
        int count = 0;
        while ((count = in.read(tmpbuf)) != -1) {
            bout.write(tmpbuf, 0, count);
            tmpbuf = new byte[1024];
        }
        in.close();
        byte[] orgData = bout.toByteArray();
        KeyPair keyPair = RSAUtil.generateKeyPair();
        RSAPublicKey pubKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey priKey = (RSAPrivateKey) keyPair.getPrivate();
        byte[] pubModBytes = pubKey.getModulus().toByteArray();
        byte[] pubPubExpBytes = pubKey.getPublicExponent().toByteArray();
        byte[] priModBytes = priKey.getModulus().toByteArray();
        byte[] priPriExpBytes = priKey.getPrivateExponent().toByteArray();
        RSAPublicKey recoveryPubKey = RSAUtil.generateRSAPublicKey(pubModBytes, pubPubExpBytes);
        RSAPrivateKey recoveryPriKey = RSAUtil.generateRSAPrivateKey(priModBytes, priPriExpBytes);
        byte[] raw = RSAUtil.encrypt(priKey, orgData);
        file = new File("encrypt_result.dat");
        OutputStream out = new FileOutputStream(file);
        out.write(raw);
        out.close();
        byte[] data = RSAUtil.decrypt(recoveryPubKey, raw);
        file = new File("decrypt_result.html");
        out = new FileOutputStream(file);
        out.write(data);
        out.flush();
        out.close();
    }
