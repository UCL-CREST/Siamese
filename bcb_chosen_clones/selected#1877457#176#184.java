    public void setContent(InputStream is) throws IOException {
        OutputStream os = null;
        try {
            os = new FileOutputStream(getDataFile());
            IOUtils.copyLarge(is, os);
        } finally {
            IOUtils.closeQuietly(os);
        }
    }
