    public String getText() throws IOException {
        InputStreamReader r = new InputStreamReader(getInputStream(), encoding);
        StringWriter w = new StringWriter(256 * 128);
        try {
            IOUtils.copy(r, w);
        } finally {
            IOUtils.closeQuietly(w);
            IOUtils.closeQuietly(r);
        }
        return w.toString();
    }
