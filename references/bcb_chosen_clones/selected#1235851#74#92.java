    public synchronized byte[] encrypt(String plainText) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidAlgorithmParameterException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream bais = new ByteArrayInputStream(plainText.getBytes("UTF8"));
        byte[] iv = new byte[8];
        Random r = new Random(System.currentTimeMillis());
        r.nextBytes(iv);
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv);
        baos.write(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParamSpec);
        CipherOutputStream cos = new CipherOutputStream(baos, cipher);
        byte[] buffer = new byte[2048];
        int bytesRead;
        while ((bytesRead = bais.read(buffer)) != -1) {
            cos.write(buffer, 0, bytesRead);
        }
        cos.close();
        byte[] result = baos.toByteArray();
        return result;
    }
