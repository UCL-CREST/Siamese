    private void write() throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(SETTINGS_ENCRYPTION_ALGORITHM);
        SecretKey passwordKey = factory.generateSecret(new PBEKeySpec(password));
        Cipher cipher = Cipher.getInstance(SETTINGS_ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, passwordKey);
        SealedObject object = new SealedObject(this, cipher);
        ObjectOutputStream privateStream = new ObjectOutputStream(FileUtils.openOutputStream(getFile()));
        try {
            privateStream.writeObject(object);
        } finally {
            privateStream.close();
        }
    }
