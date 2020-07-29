    public void outputCitation(OutputStream outputStream, DataResourceAuditor resultsOutputter, String citationFileName, Locale locale, String hostUrl) throws IOException {
        if (outputStream instanceof ZipOutputStream) {
            ((ZipOutputStream) outputStream).putNextEntry(new ZipEntry(citationFileName));
        }
        Map<String, String> dataResources = resultsOutputter.getDataResources();
        CitationCreator cc = new CitationCreator();
        StringBuffer csb = cc.createCitation(dataResources, messageSource, locale, hostUrl + datasetBaseUrl);
        outputStream.write(csb.toString().getBytes());
        if (outputStream instanceof ZipOutputStream) {
            ((ZipOutputStream) outputStream).closeEntry();
        }
    }
