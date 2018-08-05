    private void copyIntoFile(String resource, File output) throws IOException {
        FileOutputStream out = null;
        InputStream in = null;
        try {
            out = FileUtils.openOutputStream(output);
            in = GroovyInstanceTest.class.getResourceAsStream(resource);
            IOUtils.copy(in, out);
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
        }
    }
