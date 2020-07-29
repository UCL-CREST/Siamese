    private void createNewZipEntry(FileInfo fileInfo, ZipOutputStream zout) throws IOException {
        byte[] buf = new byte[BUFFER_SIZE];
        ZipEntry zipEntry = new ZipEntry(fileInfo.getName());
        zout.putNextEntry(zipEntry);
        int len;
        InputStream is = getFileInputStream(fileInfo);
        while ((len = is.read(buf)) > 0) {
            zout.write(buf, 0, len);
        }
    }
