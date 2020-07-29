    public boolean WriteFile(java.io.Serializable inObj, String fileName) throws Exception {
        FileOutputStream out;
        try {
            SecretKey skey = null;
            AlgorithmParameterSpec aps;
            out = new FileOutputStream(fileName);
            cipher = Cipher.getInstance(algorithm);
            KeySpec kspec = new PBEKeySpec(filePasswd.toCharArray());
            SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
            skey = skf.generateSecret(kspec);
            MessageDigest md = MessageDigest.getInstance(res.getString("MD5"));
            md.update(filePasswd.getBytes());
            byte[] digest = md.digest();
            System.arraycopy(digest, 0, salt, 0, 8);
            aps = new PBEParameterSpec(salt, iterations);
            out.write(salt);
            ObjectOutputStream s = new ObjectOutputStream(out);
            cipher.init(Cipher.ENCRYPT_MODE, skey, aps);
            SealedObject so = new SealedObject(inObj, cipher);
            s.writeObject(so);
            s.flush();
            out.close();
        } catch (Exception e) {
            Log.out("fileName=" + fileName);
            Log.out("algorithm=" + algorithm);
            Log.out(e);
            throw e;
        }
        return true;
    }
