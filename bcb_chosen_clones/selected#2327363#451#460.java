    public void write(URL exportUrl, OutputStream output) throws Exception {
        if (exportUrl == null || output == null) {
            throw new RuntimeException("null passed in for required parameters");
        }
        MediaContent mc = new MediaContent();
        mc.setUri(exportUrl.toString());
        MediaSource ms = service.getMedia(mc);
        InputStream input = ms.getInputStream();
        IOUtils.copy(input, output);
    }
