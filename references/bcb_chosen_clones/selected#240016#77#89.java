    public static void encryptText(OutputStream out, String text) throws CryptionException {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(pass, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            GZIPOutputStream zipOut = new GZIPOutputStream(new CipherOutputStream(out, cipher));
            zipOut.write(text.getBytes(CHARACTOR_CODE));
            zipOut.flush();
            zipOut.close();
        } catch (Exception e) {
            throw new CryptionException(e.getMessage());
        }
    }
