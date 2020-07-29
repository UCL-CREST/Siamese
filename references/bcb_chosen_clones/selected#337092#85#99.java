    public BufferedImage processUsingTemp(InputStream input, DjatokaDecodeParam params) throws DjatokaException {
        File in;
        try {
            in = File.createTempFile("tmp", ".jp2");
            FileOutputStream fos = new FileOutputStream(in);
            in.deleteOnExit();
            IOUtils.copyStream(input, fos);
        } catch (IOException e) {
            logger.error(e, e);
            throw new DjatokaException(e);
        }
        BufferedImage bi = process(in.getAbsolutePath(), params);
        if (in != null) in.delete();
        return bi;
    }
