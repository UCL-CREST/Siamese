    private byte[] gettEXtBytes(String keyword, String content) {
        byte[] keywordBytes = getISO8859_1Bytes(keyword);
        byte[] contentBytes = getISO8859_1Bytes(content);
        byte[] array = new byte[keywordBytes.length + contentBytes.length + 13];
        ByteBuffer buffer = ByteBuffer.wrap(array);
        buffer.putInt(keywordBytes.length + contentBytes.length + 1);
        buffer.put(getISO8859_1Bytes(CHUNK_TYPE_tEXT));
        buffer.put(keywordBytes);
        buffer.put((byte) 0);
        buffer.put(contentBytes);
        CRC32 crc = new CRC32();
        crc.update(array, 4, keywordBytes.length + contentBytes.length + 5);
        buffer.putInt((int) crc.getValue());
        return array;
    }
