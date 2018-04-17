    public long generateChecksum(int len, int trailer_len) {
        long cksum = -1;
        try {
            int map_file_length = (len + trailer_len);
            setBytesToCheck(map_file_length);
            seek(0);
            byte[] mapData = new byte[map_file_length];
            this.rf.read(mapData);
            CRC32 crc32 = new CRC32();
            crc32.update(mapData);
            cksum = crc32.getValue();
            resetBytesChecked();
            setBytesToCheck(0);
        } catch (IOException e) {
            pErr(e);
        }
        return cksum;
    }
