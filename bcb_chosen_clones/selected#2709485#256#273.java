    protected String getFileContentAsString(String filePath, String encoding) throws IOException {
        URL testURL = Thread.currentThread().getContextClassLoader().getResource(filePath);
        InputStream input = null;
        StringWriter sw = new StringWriter();
        try {
            if (testURL != null) {
                input = testURL.openStream();
            } else {
                input = new FileInputStream(filePath);
            }
            IOUtils.copy(input, sw, encoding);
        } finally {
            if (input != null) {
                input.close();
            }
        }
        return sw.toString();
    }
