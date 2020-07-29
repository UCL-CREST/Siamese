    public void createNewFile(String filePath, InputStream in) throws IOException {
        FileOutputStream out = null;
        try {
            File file = newFileRef(filePath);
            FileHelper.createNewFile(file, true);
            out = new FileOutputStream(file);
            IOUtils.copy(in, out);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }
