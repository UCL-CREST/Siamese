    public void generateKeyPair(String type, int bits, String filename, String username, String passphrase) throws IOException {
        System.out.println("****Sshtools.com SSH Key Pair Generator****");
        String keyType = type;
        if (keyType.equalsIgnoreCase("DSA")) {
            keyType = "ssh-dss";
        }
        if (keyType.equalsIgnoreCase("RSA")) {
            keyType = "ssh-rsa";
        }
        final SshKeyPair pair = SshKeyPairFactory.newInstance(keyType);
        System.out.println("Generating " + String.valueOf(bits) + " bit " + keyType + " key pair");
        Thread thread = new SshThread(new Runnable() {

            public void run() {
                pair.generate(SshKeyGenerator.this.bits);
            }
        }, "Key generator", true);
        thread.start();
        while (thread.isAlive()) {
            System.out.print(".");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        System.out.println();
        System.out.println("Creating Public Key file " + filename + ".pub");
        SshPublicKeyFile pub = SshPublicKeyFile.create(pair.getPublicKey(), new SECSHPublicKeyFormat(username, String.valueOf(bits) + "-bit " + type));
        FileOutputStream out = new FileOutputStream(filename + ".pub");
        out.write(pub.getBytes());
        out.close();
        System.out.println("Generating Private Key file " + filename);
        if (passphrase == null) {
            passphrase = promptForPassphrase(true);
        }
        SshPrivateKeyFile prv = SshPrivateKeyFile.create(pair.getPrivateKey(), passphrase, new SshtoolsPrivateKeyFormat(username, String.valueOf(bits) + "-bit " + type));
        out = new FileOutputStream(filename);
        out.write(prv.getBytes());
        out.close();
    }
