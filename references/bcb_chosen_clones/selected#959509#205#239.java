    public synchronized void encryptArray(char[] password, char[] charsToEncrypt, String fileOutput) throws Exception {
        if (DEBUG) {
            System.out.println("Loading the key.");
        }
        Key key = loadKey(password);
        if (DEBUG) {
            System.out.println("Loaded the key.");
        }
        Cipher cipher = Cipher.getInstance("Rijndael/CBC/PKCS5Padding");
        if (DEBUG) {
            System.out.println("Initializing SecureRandom...");
        }
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[16];
        random.nextBytes(iv);
        FileOutputStream fos = new FileOutputStream(fileOutput);
        fos.write(iv);
        IvParameterSpec spec = new IvParameterSpec(iv);
        if (DEBUG) {
            System.out.println("Initializing the cipher.");
            System.out.println(cipher.getAlgorithm());
            System.out.println("blocksize: " + cipher.getBlockSize());
        }
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);
        if (DEBUG) {
            System.out.println("Encrypting the Text...");
        }
        int theByte = 0;
        for (int i = 0; i < charsToEncrypt.length; i++) {
            theByte = charsToEncrypt[i];
            cos.write(theByte);
        }
        cos.close();
    }
