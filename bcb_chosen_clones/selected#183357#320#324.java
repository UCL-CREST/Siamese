    protected void echo(Class<?> clazz, String name, Writer out) throws IOException {
        Reader r = ResourceUtils.openReader(clazz, name);
        IOUtils.copyTo(r, out);
        r.close();
    }
