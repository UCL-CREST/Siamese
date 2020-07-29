    public void save(InputStream is) throws IOException {
        File dest = Config.getDataFile(getInternalDate(), getPhysMessageID());
        OutputStream os = null;
        try {
            os = new FileOutputStream(dest);
            IOUtils.copyLarge(is, os);
        } finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(is);
        }
    }
