    private String extractFileFromZip(ZipFile zip, String fileName) throws IOException {
        String contents = null;
        ZipEntry entry = zip.getEntry(fileName);
        if (entry != null) {
            InputStream input = zip.getInputStream(entry);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            IOUtils.copyAndClose(input, buffer);
            contents = buffer.toString();
        }
        return contents;
    }
