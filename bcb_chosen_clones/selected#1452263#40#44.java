    protected byte[] getCRC32(byte[] badata) {
        CRC32 crcgen = new CRC32();
        crcgen.update(badata);
        return Byteutils.crc32tobytearr(crcgen.getValue());
    }
