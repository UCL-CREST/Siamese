    private void writeHeader(DataOutputStream out) throws IOException {
        byte[] header = createHeader();
        out.writeInt((header.length - 4));
        out.write(header);
        CRC32 crc = new CRC32();
        crc.reset();
        crc.update(header);
        out.writeInt((int) crc.getValue());
    }
