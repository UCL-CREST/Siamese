    public static CRC32DataMessage GetCRC32DataMessage(byte buffer[], int byteCount, int sessionID) {
        CRC32DataMessage crc32DataMessage = new CRC32DataMessage();
        crc32DataMessage.sessionID = sessionID;
        crc32DataMessage.buffer = SrcConnectionHandler.GetBuffer(buffer, byteCount);
        CRC32 crc32 = new CRC32();
        crc32.update(crc32DataMessage.buffer);
        crc32DataMessage.crc32 = crc32.getValue();
        return crc32DataMessage;
    }
