    @Override
    public void encrypt(final InputStream is, OutputStream os, final Key key, final int bufferSize) throws InvalidKeyException, InvalidAlgorithmParameterException, IOException {
        if (log.isDebugEnabled()) log.debug(HelperLog.methodStart(is, os, key, bufferSize));
        if (null == is) {
            throw new RuntimeExceptionIsNull("is");
        }
        if (null == os) {
            throw new RuntimeExceptionIsNull("os");
        }
        if (null == key) {
            throw new RuntimeExceptionIsNull("key");
        }
        if (1 > bufferSize) {
            throw new RuntimeExceptionMustBeGreater("bufferSize", bufferSize, 1);
        }
        if (bufferSize > HelperEnvironment.getMemoryFree()) {
            throw new RuntimeExceptionExceedsVmMemory("bufferSize", bufferSize);
        }
        final byte[] buffer = new byte[bufferSize];
        cipher.init(Cipher.ENCRYPT_MODE, key, prepareIv());
        os = new CipherOutputStream(os, cipher);
        try {
            int offset;
            while (0 <= (offset = is.read(buffer))) {
                os.write(buffer, 0, offset);
            }
        } finally {
            os.close();
        }
        if (log.isDebugEnabled()) log.debug(HelperLog.methodExit());
    }
