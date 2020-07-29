    private static void encrypt(SecretKey key, InputStream in, OutputStream out) throws AesEncryptionException {
        byte[] buf = new byte[1024];
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            out = new CipherOutputStream(out, cipher);
            int numRead = 0;
            while ((numRead = in.read(buf)) >= 0) {
                out.write(buf, 0, numRead);
            }
        } catch (Exception e) {
            throw new AesEncryptionException(e);
        }
    }
