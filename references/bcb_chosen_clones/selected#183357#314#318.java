    protected void echo(File file, Writer out) throws IOException {
        FileReader r = new FileReader(file);
        IOUtils.copyTo(r, out);
        r.close();
    }
