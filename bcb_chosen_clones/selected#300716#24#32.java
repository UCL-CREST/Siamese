    @Override
    public void write(OutputStream output) throws IOException, WebApplicationException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final GZIPOutputStream gzipOs = new GZIPOutputStream(baos);
        IOUtils.copy(is, gzipOs);
        baos.close();
        gzipOs.close();
        output.write(baos.toByteArray());
    }
