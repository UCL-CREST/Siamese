    public static void writeChunk(OutputStream os, byte[] buf, int offset, int n, byte[] chunkid, CRC32 crc) {
        if (chunkid.length != 4) throw new PngOutputException("bad chunkid [" + new String(chunkid) + "]");
        int chunkLength = n;
        crc.reset();
        PngHelper.writeInt4(os, chunkLength);
        PngHelper.writeBytes(os, chunkid);
        PngHelper.writeBytes(os, buf, 0, n);
        crc.update(chunkid, 0, chunkid.length);
        crc.update(buf, 0, n);
        PngHelper.writeInt4(os, (int) crc.getValue());
    }
