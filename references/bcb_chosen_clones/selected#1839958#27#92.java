    public static HerbivoreChallenge compute(String aname, int m) {
        MessageDigest md = null, md2 = null;
        Random rng;
        long val;
        int count = 0;
        try {
            byte[] publicKeyBytes = null;
            byte[] privateKeyBytes = null;
            byte[] yKeyBytes = new byte[8];
            PublicKey publicKey = new RawRSAPublicKey(new FileInputStream(aname + ".pubkey"));
            publicKeyBytes = publicKey.getEncoded();
            PrivateKey privateKey = new RawRSAPrivateKey(new FileInputStream(aname + ".privkey"));
            privateKeyBytes = privateKey.getEncoded();
            (new FileInputStream(aname + ".puzzlekey")).read(yKeyBytes);
            Log.info("Read public/private/puzzle keys from disk...");
            return new HerbivoreChallenge(aname, new HerbivoreRSAKeyPair(publicKeyBytes, privateKeyBytes), yKeyBytes);
        } catch (Exception e) {
            System.out.println("Can't read public/private/puzzle keys from file.");
        }
        Log.info("Generating public/private/puzzle keys...");
        rng = new Random();
        long startTime = System.currentTimeMillis();
        if (m > Y_LEN) return null;
        try {
            md = MessageDigest.getInstance("SHA");
            md2 = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("No SHA support!");
        }
        HerbivoreRSAKeyPair pair = HerbivoreRSA.generateKeyPair();
        md.update(pair.getPublicKey());
        byte[] digest = md.digest();
        byte[] digest2 = null;
        boolean match = false;
        byte raw[] = new byte[8];
        while (!match) {
            count++;
            val = rng.nextLong();
            for (int i = 0; i < 8; i++) {
                raw[i] = (byte) (val & 0xff);
                val >>= 8;
            }
            md2.reset();
            md2.update(raw);
            digest2 = md2.digest();
            match = HerbivoreUtil.equalsBits(digest, digest2, m);
        }
        long ms = System.currentTimeMillis() - startTime;
        System.out.println("It took " + count + " tries " + ms + " ms for a " + m + " bit match");
        try {
            FileOutputStream fos = null;
            System.out.println("Saving keys to disk...");
            fos = new FileOutputStream(aname + ".pubkey");
            fos.write(pair.getPublicKey());
            fos.close();
            fos = new FileOutputStream(aname + ".privkey");
            fos.write(pair.getPrivateKey());
            fos.close();
            fos = new FileOutputStream(aname + ".puzzlekey");
            fos.write(raw);
            fos.close();
        } catch (Exception e) {
            System.out.println("Can't save keys to file.");
        }
        return new HerbivoreChallenge(aname, pair, raw);
    }
