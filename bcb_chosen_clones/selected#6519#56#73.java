    public boolean isValid(byte[] bytes) {
        ByteArrayInputStream sourceID = new ByteArrayInputStream(bytes);
        DataInputStream sourceIDStream = new DataInputStream(sourceID);
        byte[] content = new byte[bytes.length - Long.SIZE];
        long storedCRC;
        long contentCRC;
        try {
            sourceIDStream.read(content, Long.SIZE, bytes.length - Long.SIZE);
            CRC32 crc32 = new CRC32();
            crc32.update(content);
            storedCRC = sourceIDStream.readLong();
            contentCRC = crc32.getValue();
            sourceIDStream.close();
        } catch (IOException e) {
            return false;
        }
        return contentCRC == storedCRC;
    }
