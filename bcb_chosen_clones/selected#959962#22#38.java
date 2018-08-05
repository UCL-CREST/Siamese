    public String encrypt(Serializable source) {
        SecretKeySpec spec = getSecretKeySpec();
        try {
            Cipher c = Cipher.getInstance(spec.getAlgorithm());
            c.init(Cipher.ENCRYPT_MODE, spec);
            SealedObject so = new SealedObject(source, c);
            ByteArrayOutputStream store = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(store);
            out.writeObject(so);
            out.close();
            byte[] data = store.toByteArray();
            byte[] textData = Base64.encode(data);
            return new String(textData, "US-ASCII");
        } catch (Exception e) {
            return null;
        }
    }
