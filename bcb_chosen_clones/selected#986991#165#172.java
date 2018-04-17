    protected void setTestContent(IDfDocument document, String testFileName) throws Exception {
        InputStream testFileIs = new BufferedInputStream(FileHelper.getFileAsStreamFromClassPath(testFileName));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(testFileIs, baos);
        String contentType = formatHelper.getFormatForExtension(FileHelper.getFileExtension(testFileName));
        document.setContentType(contentType);
        document.setContent(baos);
    }
