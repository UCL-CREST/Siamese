    public void send(DataMessage dataMessage) throws IOException {
        if (dataMessage instanceof CRC32DataMessage) {
            CRC32DataMessage crc32DataMessage = (CRC32DataMessage) dataMessage;
            CRC32 crc32 = new CRC32();
            crc32.update(dataMessage.buffer);
            if (crc32.getValue() != crc32DataMessage.crc32) {
                throw new IOException("CRC32 value is incorrect on the data received from the server (Expected " + crc32DataMessage.crc32 + ", Found " + crc32.getValue() + ")");
            }
        }
        os.write(dataMessage.buffer);
        os.flush();
    }
