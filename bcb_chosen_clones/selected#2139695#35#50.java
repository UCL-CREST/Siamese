    public void decryptFile(String encryptedFile, String decryptedFile, String password) throws Exception {
        CipherInputStream in;
        OutputStream out;
        Cipher cipher;
        SecretKey key;
        byte[] byteBuffer;
        cipher = Cipher.getInstance("DES");
        key = new SecretKeySpec(password.getBytes(), "DES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        in = new CipherInputStream(new FileInputStream(encryptedFile), cipher);
        out = new FileOutputStream(decryptedFile);
        byteBuffer = new byte[1024];
        for (int n; (n = in.read(byteBuffer)) != -1; out.write(byteBuffer, 0, n)) ;
        in.close();
        out.close();
    }
