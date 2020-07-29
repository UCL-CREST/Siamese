    public boolean setSourceIDFromByteArray(byte[] bytes) {
        if (bytes == null) return false;
        ByteArrayInputStream sourceID = new ByteArrayInputStream(bytes);
        DataInputStream sourceIDStream = new DataInputStream(sourceID);
        byte[] content = new byte[bytes.length - (Long.SIZE >> 3)];
        long storedCRC;
        long contentCRC;
        try {
            sourceIDStream.read(content, 0, content.length);
            CRC32 crc32 = new CRC32();
            crc32.update(content);
            storedCRC = sourceIDStream.readLong();
            contentCRC = crc32.getValue();
            sourceIDStream.close();
            if (contentCRC == storedCRC) {
                sourceID = new ByteArrayInputStream(content);
                sourceIDStream = new DataInputStream(sourceID);
                sourceIDStream.read(mac, 0, 6);
                sourceIDStream.read(ip, 0, 4);
                userId = sourceIDStream.readLong();
                date = new Date(sourceIDStream.readLong());
                revision = sourceIDStream.readLong();
                ID = sourceIDStream.readLong();
                sessionId = sourceIDStream.readLong();
                crc = contentCRC;
            }
        } catch (IOException e) {
            return false;
        }
        return contentCRC == storedCRC;
    }
