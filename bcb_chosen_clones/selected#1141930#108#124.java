    public byte[] encrypt(String inString) throws CryptoException {
        byte[] theEncryptedBytes = null;
        if (inString != null) {
            try {
                cipher.init(Cipher.ENCRYPT_MODE, key, pbeParamSpec);
                ByteArrayOutputStream theBAOS = new ByteArrayOutputStream();
                CipherOutputStream theCOS = new CipherOutputStream(theBAOS, cipher);
                theCOS.write(inString.getBytes());
                theCOS.close();
                theBAOS.close();
                theEncryptedBytes = theBAOS.toByteArray();
            } catch (Exception e) {
                throw new CryptoException(e);
            }
        }
        return theEncryptedBytes;
    }
