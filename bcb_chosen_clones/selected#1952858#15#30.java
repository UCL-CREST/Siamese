    public static void encryptFile(String input, String output, String pwd) throws Exception {
        CipherOutputStream out;
        InputStream in;
        Cipher cipher;
        SecretKey key;
        byte[] byteBuffer;
        cipher = Cipher.getInstance("DES");
        key = new SecretKeySpec(pwd.getBytes(), "DES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        in = new FileInputStream(input);
        out = new CipherOutputStream(new FileOutputStream(output), cipher);
        byteBuffer = new byte[1024];
        for (int n; (n = in.read(byteBuffer)) != -1; out.write(byteBuffer, 0, n)) ;
        in.close();
        out.close();
    }
