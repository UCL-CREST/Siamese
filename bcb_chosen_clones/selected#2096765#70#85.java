    public void encryptToFile(Map<String, String> propMap, File file) throws IOException {
        if (cipher == null || cipherKey == null) {
            throw new IllegalStateException("Cipher not properly initialized");
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, cipherKey);
            CipherOutputStream cos = new CipherOutputStream(new FileOutputStream(file), cipher);
            ObjectOutputStream oos = new ObjectOutputStream(cos);
            oos.writeObject(propMap);
            oos.flush();
            oos.close();
            cos.close();
        } catch (InvalidKeyException exc) {
            throw new IllegalStateException(exc);
        }
    }
