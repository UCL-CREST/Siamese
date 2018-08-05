    public String openFileAsText(String resource) throws IOException {
        StringWriter wtr = new StringWriter();
        InputStreamReader rdr = new InputStreamReader(openFile(resource));
        try {
            IOUtils.copy(rdr, wtr);
        } finally {
            IOUtils.closeQuietly(rdr);
        }
        return wtr.toString();
    }
