    @Override
    public String encryptString(String passphrase, String message) throws Exception {
        MessageDigest md;
        md = MessageDigest.getInstance("MD5");
        md.update(passphrase.getBytes("UTF-8"));
        byte digest[] = md.digest();
        String digestString = base64encode(digest);
        System.out.println(digestString);
        SecureRandom sr = new SecureRandom(digestString.getBytes());
        KeyGenerator kGen = KeyGenerator.getInstance("AES");
        kGen.init(128, sr);
        Key key = kGen.generateKey();
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] bIn = cipher.doFinal(message.getBytes("UTF-8"));
        String base64Encoded = base64encode(bIn);
        return base64Encoded;
    }
