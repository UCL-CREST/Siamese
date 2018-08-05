    public static void main(String[] args) throws Exception {
        KeyPair keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        for (int i = 0; i < 100; i++) {
            String data = "www.tutorials.de";
            System.out.println("Plain text data: " + data);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
            ByteArrayOutputStream baosEncryptedData = new ByteArrayOutputStream();
            CipherOutputStream cos = new CipherOutputStream(baosEncryptedData, cipher);
            cos.write(data.getBytes("UTF-8"));
            cos.flush();
            cos.close();
            System.out.println("Encrypted data: " + new String(baosEncryptedData.toByteArray(), "UTF-8"));
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            CipherInputStream cis = new CipherInputStream(new ByteArrayInputStream(baosEncryptedData.toByteArray()), cipher);
            ByteArrayOutputStream baosDecryptedData = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int len = 0;
            while ((len = cis.read(buffer)) > 0) {
                baosDecryptedData.write(buffer, 0, len);
            }
            baosDecryptedData.flush();
            cis.close();
            System.out.println("Decrypted data: " + new String(baosDecryptedData.toByteArray(), "UTF-8"));
        }
    }
