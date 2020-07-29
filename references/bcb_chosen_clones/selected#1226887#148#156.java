    public static void writeFullImageToStream(Image scaledImage, String javaFormat, OutputStream os) throws IOException {
        BufferedImage bufImage = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null), BufferedImage.TYPE_BYTE_BINARY);
        Graphics gr = bufImage.getGraphics();
        gr.drawImage(scaledImage, 0, 0, null);
        gr.dispose();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bufImage, javaFormat, bos);
        IOUtils.copyStreams(new ByteArrayInputStream(bos.toByteArray()), os);
    }
