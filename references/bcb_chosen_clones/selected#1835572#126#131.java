    private void writeIndexFile() throws IOException {
        zipOut.putNextEntry(new ZipEntry(INDEX_FILENAME));
        String contents = StringUtils.findAndReplace(INDEX_CONTENTS, "DEFAULT_FILE", defaultPath);
        zipOut.write(contents.getBytes(HTTPUtils.DEFAULT_CHARSET));
        zipOut.closeEntry();
    }
