    @Override
    public byte[] encrypt(byte[] data, byte[] keySignature) throws CryptographicException {
        tmpData = new byte[data.length];
        System.arraycopy(data, 0, tmpData, 0, data.length);
        KeyBL key = KeyCache.searchKey(keySignature);
        if (key == null) {
            return null;
        }
        int dataSize = data.length;
        if (log.isTraceEnabled()) {
            log.trace("Encrypting " + dataSize + " bytes with CSP " + key.getKeyCSP());
        }
        BlockCipherBL blockCipher = CSPManager.getBlockCipher(key.getKeyCSP());
        if (blockCipher == null) {
            throw new CryptographicException("Cryptographic service provider " + key.getKeyCSP() + " couldn't be found");
        }
        int blockSize = blockCipher.getBlockSize();
        int cryptedDataSize = dataSize + 8;
        int cryptedDataBlockCount = (cryptedDataSize + blockSize - 1) / blockSize;
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        int crcValue = (int) crc32.getValue();
        byte[] cryptedData = new byte[blockSize * cryptedDataBlockCount];
        System.arraycopy(data, 0, cryptedData, 0, dataSize);
        intToBytes(cryptedData, cryptedData.length - 8, dataSize);
        intToBytes(cryptedData, cryptedData.length - 4, crcValue);
        blockCipher.getBlockCipher().encrypt(cryptedData, 0, blockSize, cryptedDataBlockCount, key.getKey());
        return cryptedData;
    }
