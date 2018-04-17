    private byte[] getIENDBytes() {
        byte[] array = new byte[12];
        byte[] typeBytes = getISO8859_1Bytes(CHUNK_TYPE_IEND);
        ByteBuffer buffer = ByteBuffer.wrap(array);
        buffer.putInt(0);
        buffer.put(typeBytes);
        CRC32 crc = new CRC32();
        crc.update(typeBytes);
        buffer.putInt((int) crc.getValue());
        return array;
    }
