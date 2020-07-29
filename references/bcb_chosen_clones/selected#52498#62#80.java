    @Override
    public String decryptString(String passphrase, String crypted) throws Exception {
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
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] cryptString = base64decode(crypted);
        byte[] bOut = cipher.doFinal(cryptString);
        String outString = new String(bOut, "UTF-8");
        return outString;
    }
