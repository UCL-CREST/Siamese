    public synchronized void encrypt(char[] password, String fileInput, String fileOutput) throws Exception {
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
        FileInputStream fis = new FileInputStream(fileInput);
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
            System.out.println("Encrypting the file...");
        }
        int theByte = 0;
        while ((theByte = fis.read()) != -1) {
            cos.write(theByte);
        }
        fis.close();
        cos.close();
    }
