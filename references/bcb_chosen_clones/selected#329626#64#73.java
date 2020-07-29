    public void dumpToFile(File file) throws IOException {
        OutputStream out = new FileOutputStream(file);
        if (contentStream != null) {
            IOUtils.copy(contentStream, out);
            setPath(file.getAbsolutePath());
        } else {
            IOUtils.write(getContent(), out);
        }
        IOUtils.closeQuietly(out);
    }
