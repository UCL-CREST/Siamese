    private void initIdentity(String p_file) throws Exception {
        File x_ks_file = new File(p_file);
        if (x_ks_file.exists() == true) {
            FileInputStream x_fis = new FileInputStream(x_ks_file);
            if (x_fis == null) System.err.println("arg");
            ObjectInputStream s = new ObjectInputStream(x_fis);
            o_priv = (PrivateKey) (s.readObject());
            o_pub = (PublicKey) (s.readObject());
            s.close();
        } else {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(1024, random);
            KeyPair pair = keyGen.generateKeyPair();
            o_priv = pair.getPrivate();
            o_pub = pair.getPublic();
            FileOutputStream x_fos = new FileOutputStream(x_ks_file);
            if (x_fos == null) System.err.println("arg");
            ObjectOutputStream s = new ObjectOutputStream(x_fos);
            s.writeObject(o_priv);
            s.writeObject(o_pub);
            s.flush();
            s.close();
        }
    }
