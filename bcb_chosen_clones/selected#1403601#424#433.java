    private RandomAccessFile createInvalidDataFile() throws IOException {
        RandomAccessFile invalidDataFile = new RandomAccessFile(fileName + ".invalid" + Long.toHexString(System.currentTimeMillis()), "rw");
        final byte[] headerBuf = dataFileHeaderFieldsToBytes();
        CRC32 crc32 = new CRC32();
        crc32.update(headerBuf);
        invalidDataFile.seek(0L);
        invalidDataFile.write(headerBuf);
        invalidDataFile.writeLong(crc32.getValue());
        return invalidDataFile;
    }
