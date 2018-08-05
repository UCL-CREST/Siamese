    public void writeFile(String resource, InputStream is) throws IOException {
        File f = prepareFsReferenceAsFile(resource);
        FileOutputStream fos = new FileOutputStream(f);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        try {
            IOUtils.copy(is, bos);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(bos);
        }
    }
