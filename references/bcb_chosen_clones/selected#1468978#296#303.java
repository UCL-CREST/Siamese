    public static long computeCRC(byte[] pBytes) throws IllegalArgumentException {
        if (null == pBytes) {
            throw new IllegalArgumentException(NO_BYTE_ARRAY);
        }
        CRC32 checksumObj = new CRC32();
        checksumObj.update(pBytes);
        return checksumObj.getValue();
    }
