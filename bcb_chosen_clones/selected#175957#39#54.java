    public byte[] encryptPassowrd(String password) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(this.algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, this.keyPair.getPublic());
            ByteArrayOutputStream baosEncryptedData = new ByteArrayOutputStream();
            CipherOutputStream cos = new CipherOutputStream(baosEncryptedData, cipher);
            cos.write(password.getBytes("UTF-8"));
            cos.flush();
            cos.close();
            return baosEncryptedData.toByteArray();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
