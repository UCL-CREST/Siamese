    private byte[] getgAMABytes() {
        byte[] array = new byte[16];
        ByteBuffer buffer = ByteBuffer.wrap(array);
        buffer.putInt(4);
        buffer.put(getISO8859_1Bytes(CHUNK_TYPE_gAMA));
        buffer.putInt(gamma);
        CRC32 crc = new CRC32();
        crc.update(array, 4, 8);
        buffer.putInt((int) crc.getValue());
        return array;
    }
