    private File writeResourceToFile(String resource) throws IOException {
        File tmp = File.createTempFile("zfppt" + resource, null);
        InputStream res = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        OutputStream out = new FileOutputStream(tmp);
        IOUtils.copy(res, out);
        out.close();
        return tmp;
    }
