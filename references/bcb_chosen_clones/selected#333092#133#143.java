    private static byte[] encryptToByteArray(Object obj, File keyFile) throws Exception {
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, keyFile);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CipherOutputStream cos = new CipherOutputStream(bos, cipher);
        ObjectOutputStream oos = new ObjectOutputStream(cos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();
        byte[] buf = bos.toByteArray();
        return buf;
    }
