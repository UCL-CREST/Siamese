    public static String encryptObjectForURL(Object source, SecretKey key) throws java.security.NoSuchAlgorithmException, java.security.InvalidKeyException, javax.crypto.NoSuchPaddingException, java.io.UnsupportedEncodingException, javax.crypto.IllegalBlockSizeException, javax.crypto.BadPaddingException, java.io.NotSerializableException {
        Cipher desCipher = Cipher.getInstance(ENCRYPT_POLICY);
        desCipher.init(Cipher.ENCRYPT_MODE, key);
        if (!(source instanceof Serializable)) throw new NotSerializableException();
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            ObjectOutputStream o = new ObjectOutputStream(b);
            o.writeObject(source);
            o.flush();
            o.close();
            return intoHexString(desCipher.doFinal(b.toByteArray()));
        } catch (IOException e) {
            throw new RuntimeException("No IO Exception should occur, this is all in-memory!!");
        }
    }
