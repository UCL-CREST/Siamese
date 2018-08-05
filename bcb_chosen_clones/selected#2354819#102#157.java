    private byte[] getIHDRBytes(BufferedImage image) {
        byte bitDepth;
        byte colorType;
        int imageType = image.getType();
        switch(imageType) {
            case BufferedImage.TYPE_3BYTE_BGR:
            case BufferedImage.TYPE_INT_BGR:
            case BufferedImage.TYPE_INT_RGB:
            case BufferedImage.TYPE_USHORT_555_RGB:
            case BufferedImage.TYPE_USHORT_565_RGB:
                bitDepth = 8;
                colorType = 2;
                break;
            case BufferedImage.TYPE_4BYTE_ABGR:
            case BufferedImage.TYPE_INT_ARGB:
                bitDepth = 8;
                colorType = 6;
                break;
            case BufferedImage.TYPE_BYTE_INDEXED:
                bitDepth = 8;
                colorType = 3;
                break;
            case BufferedImage.TYPE_BYTE_GRAY:
                bitDepth = 8;
                colorType = 0;
                break;
            case BufferedImage.TYPE_USHORT_GRAY:
                bitDepth = 16;
                colorType = 0;
                break;
            case BufferedImage.TYPE_BYTE_BINARY:
            case BufferedImage.TYPE_4BYTE_ABGR_PRE:
            case BufferedImage.TYPE_INT_ARGB_PRE:
            case BufferedImage.TYPE_CUSTOM:
            default:
                throw new RuntimeException("Unsupported image type");
        }
        byte compressionMethod = 0;
        byte filterMethod = 0;
        byte interlaceMethod = 0;
        byte[] array = new byte[25];
        ByteBuffer buffer = ByteBuffer.wrap(array);
        buffer.putInt(13);
        buffer.put(getISO8859_1Bytes(CHUNK_TYPE_IHDR));
        buffer.putInt(image.getWidth());
        buffer.putInt(image.getHeight());
        buffer.put(bitDepth);
        buffer.put(colorType);
        buffer.put(compressionMethod);
        buffer.put(filterMethod);
        buffer.put(interlaceMethod);
        CRC32 crc = new CRC32();
        crc.update(array, 4, 17);
        buffer.putInt((int) crc.getValue());
        return array;
    }
