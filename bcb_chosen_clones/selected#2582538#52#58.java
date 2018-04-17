    public String getData() throws ValueFormatException, RepositoryException, IOException {
        InputStream is = getStream();
        StringWriter sw = new StringWriter();
        IOUtils.copy(is, sw, "UTF-8");
        IOUtils.closeQuietly(is);
        return sw.toString();
    }
