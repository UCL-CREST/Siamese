    public String upload() throws IOException {
        int idx = docIndex.incrementAndGet();
        String tmpName = "namefinder/doc_" + idx + "__" + fileFileName;
        File tmpFile = tmpFile(tmpName);
        if (tmpFile.exists()) {
            org.apache.commons.io.FileUtils.deleteQuietly(tmpFile);
        }
        org.apache.commons.io.FileUtils.touch(tmpFile);
        InputStream fileStream = new FileInputStream(file);
        OutputStream bos = new FileOutputStream(tmpFile);
        IOUtils.copy(fileStream, bos);
        bos.close();
        fileStream.close();
        return tmpUrl(tmpName);
    }
