    private int computeCRC(int currentLen) {
        CRC32 crc32 = new CRC32();
        if (bb.hasArray()) {
            byte[] bytes = bb.array();
            int start = bb.arrayOffset();
            crc32.update(bytes, start, currentLen);
        } else {
            for (int index = 0; index < currentLen; index++) {
                byte b = bb.get(index);
                crc32.update(b);
            }
        }
        int crc = (int) (crc32.getValue() & 0xffffffff);
        return crc;
    }
