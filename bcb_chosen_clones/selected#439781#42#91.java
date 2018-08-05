    public byte[] generateImage(PixelSource source) throws IOException {
        int width = source.getWidth();
        int height = source.getHeight();
        CRC32 crc = new CRC32();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(HEADER);
        writeUnsignedInt(out, 13);
        ByteArrayOutputStream header = new ByteArrayOutputStream();
        header.write(IHDR);
        writeUnsignedInt(header, width);
        writeUnsignedInt(header, height);
        header.write(8);
        header.write(2);
        header.write(0);
        header.write(0);
        header.write(0);
        crc.reset();
        crc.update(header.toByteArray());
        out.write(header.toByteArray());
        writeUnsignedInt(out, crc.getValue());
        Deflater deflator = new Deflater(5);
        ByteArrayOutputStream zippedByteStream = new ByteArrayOutputStream(1024);
        BufferedOutputStream zipStream = new BufferedOutputStream(new DeflaterOutputStream(zippedByteStream, deflator));
        byte[] scanLines = new byte[width * height * BYTES_PER_PIXEL + height];
        for (int i = 0; i < width * height; i++) {
            if (i % width == 0) {
                zipStream.write(0);
            }
            int pixel = source.getPixel(i % width, i / width);
            zipStream.write((pixel >> 16) & 0xff);
            zipStream.write((pixel >> 8) & 0xff);
            zipStream.write(pixel & 0xff);
        }
        zipStream.close();
        byte[] zippedBytes = zippedByteStream.toByteArray();
        writeUnsignedInt(out, zippedBytes.length);
        out.write(IDAT);
        crc.reset();
        crc.update(IDAT);
        out.write(zippedBytes);
        crc.update(zippedBytes);
        writeUnsignedInt(out, crc.getValue());
        deflator.finish();
        writeUnsignedInt(out, 0);
        out.write(IEND);
        crc.reset();
        crc.update(IEND);
        writeUnsignedInt(out, crc.getValue());
        return out.toByteArray();
    }
