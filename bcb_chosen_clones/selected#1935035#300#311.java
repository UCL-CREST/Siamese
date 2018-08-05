    private long calcChecksum(InputStream in) throws IOException {
        CRC32 crc = new CRC32();
        int len = buffer.length;
        int count = -1;
        int haveRead = 0;
        while ((count = in.read(buffer, 0, len)) > 0) {
            haveRead += count;
            crc.update(buffer, 0, count);
        }
        in.close();
        return crc.getValue();
    }
