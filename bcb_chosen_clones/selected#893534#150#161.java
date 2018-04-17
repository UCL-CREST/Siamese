    public static void decryptFile(String infile, String outfile, String keyFile) throws Exception {
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, getKey());
        java.io.FileInputStream in = new java.io.FileInputStream(infile);
        java.io.FileOutputStream fileOut = new java.io.FileOutputStream(outfile);
        javax.crypto.CipherOutputStream out = new javax.crypto.CipherOutputStream(fileOut, cipher);
        byte[] buffer = new byte[kBufferSize];
        int length;
        while ((length = in.read(buffer)) != -1) out.write(buffer, 0, length);
        in.close();
        out.close();
    }
