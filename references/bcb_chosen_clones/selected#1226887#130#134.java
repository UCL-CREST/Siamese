    public static void writeImageToStream(BufferedImage scaledImage, String javaFormat, OutputStream os) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(scaledImage, javaFormat, bos);
        IOUtils.copyStreams(new ByteArrayInputStream(bos.toByteArray()), os);
    }
