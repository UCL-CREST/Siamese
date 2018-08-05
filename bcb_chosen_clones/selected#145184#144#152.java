    public static void readChunk(InputStream is, byte[] buff, int offset, int len, byte[] chunkid, CRC32 crc) {
        crc.reset();
        crc.update(chunkid, 0, chunkid.length);
        readBytes(is, buff, offset, len);
        crc.update(buff, offset, len);
        int crcval = (int) crc.getValue();
        int crcori = readInt4(is);
        if (crcori != crcval) throw new PngBadCrcException("crc no coincide " + new String(chunkid) + " calc=" + crcval + " read=" + crcori);
    }
