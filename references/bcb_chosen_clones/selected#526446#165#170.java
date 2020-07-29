    private String fetchCompareContent() throws IOException {
        URL url = new URL(compareTo);
        StringWriter sw = new StringWriter();
        IOUtils.copy(url.openStream(), sw);
        return sw.getBuffer().toString();
    }
