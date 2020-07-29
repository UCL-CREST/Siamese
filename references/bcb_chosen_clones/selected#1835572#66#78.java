    protected void writeEntry(String uri, String contentType, byte[] content, int offset) throws IOException {
        String safeURI = getSafeURI(uri, contentType);
        String path = getZipEntryFilename(safeURI);
        String dnType = contentType.toLowerCase();
        if (dnType.startsWith("text/html") && dnType.indexOf("charset=") != -1) {
            content = addContentTypeHTMLHeader(contentType, content, offset);
            offset = 0;
        }
        addingZipEntry(path, contentType);
        zipOut.putNextEntry(new ZipEntry(path));
        zipOut.write(content, offset, content.length - offset);
        zipOut.closeEntry();
    }
