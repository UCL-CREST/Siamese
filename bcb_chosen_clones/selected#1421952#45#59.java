    @Override
    public ChannelDataBuffer compress(ChannelDataBuffer data, ChannelDataBuffer out) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(out.getOutputStream());
        zos.putNextEntry(new ZipEntry("temp"));
        for (ByteBuffer buf : ChannelDataBuffer.asByteBuffers(data)) {
            byte[] raw = buf.array();
            int offset = buf.position();
            int len = buf.limit() - buf.position();
            zos.write(raw, offset, len);
        }
        zos.flush();
        zos.closeEntry();
        zos.close();
        return out;
    }
