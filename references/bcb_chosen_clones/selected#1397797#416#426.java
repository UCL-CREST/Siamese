    public static long getDummyCRC(long count) throws IOException {
        CRC32 crc = new CRC32();
        byte[] data = new byte[1024];
        int done = 0;
        while (done < count) {
            int tbw = (int) Math.min(count - done, data.length);
            crc.update(data, 0, tbw);
            done += tbw;
        }
        return crc.getValue();
    }
