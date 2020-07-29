    @Override
    public byte[] decrypt(byte[] cryptedData, byte[] keySignature) throws CryptographicException {
        KeyBL key = KeyCache.searchKey(keySignature);
        if (key == null) {
            return null;
        }
        BlockCipherBL blockCipher = CSPManager.getBlockCipher(key.getKeyCSP());
        int blockSize = blockCipher.getBlockSize();
        int cryptedDataSize = cryptedData.length;
        if (cryptedDataSize == 0 || cryptedDataSize % blockSize != 0) {
            throw new CryptographicException("Incompatible crypted data size (" + cryptedDataSize + ") or block cipher block size (" + blockSize + ")");
        }
        if (blockSize <= 8) {
            throw new CryptographicException("Unsupported block type: " + blockSize + " <= 8");
        }
        byte[] lastBlocks;
        if (cryptedData.length == blockSize) {
            lastBlocks = new byte[blockSize];
        } else {
            lastBlocks = new byte[2 * blockSize];
        }
        System.arraycopy(cryptedData, cryptedData.length - lastBlocks.length, lastBlocks, 0, lastBlocks.length);
        blockCipher.getBlockCipher().decrypt(lastBlocks, 0, blockSize, lastBlocks.length / blockSize, key.getKey());
        int dataSize = bytesToInt(lastBlocks, lastBlocks.length - 8);
        int crcValue = bytesToInt(lastBlocks, lastBlocks.length - 4);
        if (log.isTraceEnabled()) {
            log.trace("Decrypting " + dataSize + " bytes with CSP " + key.getKeyCSP());
        }
        if (dataSize > cryptedDataSize) {
            log.error("Decryption error");
            throw new RuntimeException("Decryption error");
        }
        byte[] data = new byte[dataSize];
        int fullDataBlockCount = data.length / blockSize;
        int dataInLastBlock = data.length % blockSize;
        if (cryptedData.length != blockSize && fullDataBlockCount == cryptedDataSize / blockSize - 1) {
            fullDataBlockCount--;
            dataInLastBlock += blockSize;
        }
        System.arraycopy(cryptedData, 0, data, 0, fullDataBlockCount * blockSize);
        blockCipher.getBlockCipher().decrypt(data, 0, blockSize, fullDataBlockCount, key.getKey());
        System.arraycopy(lastBlocks, 0, data, fullDataBlockCount * blockSize, dataInLastBlock);
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        if ((int) crc32.getValue() != crcValue) {
            throw new CryptographicException("CRC error after decryption. (should: " + crcValue + ", is: " + (int) crc32.getValue() + ")");
        }
        return data;
    }
