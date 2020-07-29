    public static void test2() throws Exception {
        int keySize = 1024;
        int dBlockSize = keySize / 8;
        int eBlockSize = dBlockSize - 8 - 3;
        CertAndKeyGen certAndKeyGen = new CertAndKeyGen("RSA", "MD5WithRSA");
        certAndKeyGen.generate(keySize);
        PublicKey publicKey = certAndKeyGen.getPublicKey();
        PrivateKey privateKey = certAndKeyGen.getPrivateKey();
        Cipher cipher1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher1.init(Cipher.ENCRYPT_MODE, publicKey);
        String fileA = "C:/temp/a.txt";
        String fileB = "C:/temp/b.txt";
        String fileC = "C:/temp/c.txt";
        FileInputStream fis = new FileInputStream(fileA);
        FileOutputStream fos = new FileOutputStream(fileB, false);
        CipherOutputStream eos = new CipherOutputStream(fos, cipher1, eBlockSize);
        byte[] b = new byte[128];
        int i = fis.read(b);
        while (i != -1) {
            eos.write(b, 0, i);
            i = fis.read(b);
        }
        eos.flush();
        eos.close();
        fos.close();
        Cipher cipher2 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher2.init(Cipher.DECRYPT_MODE, privateKey);
        fis = new FileInputStream(fileB);
        CipherInputStream cis = new CipherInputStream(fis, cipher2, dBlockSize);
        FileOutputStream decodedFile = new FileOutputStream(fileC, false);
        int read = -1;
        while ((read = cis.read()) > -1) {
            decodedFile.write(read);
        }
        decodedFile.close();
        fis.close();
    }
