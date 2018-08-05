    private byte[] getIDATBytes(BufferedImage image) {
        byte[] imageData;
        int imageType = image.getType();
        switch(imageType) {
            case BufferedImage.TYPE_3BYTE_BGR:
            case BufferedImage.TYPE_INT_BGR:
            case BufferedImage.TYPE_INT_RGB:
            case BufferedImage.TYPE_USHORT_555_RGB:
            case BufferedImage.TYPE_USHORT_565_RGB:
                imageData = getRGBBytes(image);
                break;
            case BufferedImage.TYPE_4BYTE_ABGR:
                imageData = getABGRBytes(image);
                break;
            case BufferedImage.TYPE_INT_ARGB:
                imageData = getARGBBytes(image);
                break;
            case BufferedImage.TYPE_BYTE_INDEXED:
            case BufferedImage.TYPE_BYTE_GRAY:
                imageData = get8BitSampleBytes(image);
                break;
            case BufferedImage.TYPE_USHORT_GRAY:
                imageData = get16BitSampleBytes(image);
                break;
            case BufferedImage.TYPE_BYTE_BINARY:
            case BufferedImage.TYPE_4BYTE_ABGR_PRE:
            case BufferedImage.TYPE_INT_ARGB_PRE:
            case BufferedImage.TYPE_CUSTOM:
            default:
                throw new RuntimeException("Unsupported image type");
        }
        byte[] compressedImageData = new byte[imageData.length];
        Deflater deflater = new Deflater();
        deflater.setInput(imageData);
        deflater.finish();
        int compressedSize = deflater.deflate(compressedImageData);
        byte[] array = new byte[compressedSize + 12];
        ByteBuffer buffer = ByteBuffer.wrap(array);
        buffer.putInt(compressedSize);
        buffer.put(getISO8859_1Bytes(CHUNK_TYPE_IDAT));
        buffer.put(compressedImageData, 0, compressedSize);
        CRC32 crc = new CRC32();
        crc.update(array, 4, compressedSize + 4);
        buffer.putInt((int) crc.getValue());
        return array;
    }
