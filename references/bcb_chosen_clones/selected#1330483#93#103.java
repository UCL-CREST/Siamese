    public void createFile(File src, String filename) throws IOException {
        try {
            FileInputStream fis = new FileInputStream(src);
            OutputStream fos = this.fileResourceManager.writeResource(this.txId, filename);
            IOUtils.copy(fis, fos);
            fos.close();
            fis.close();
        } catch (ResourceManagerException e) {
            LOGGER.error(e);
        }
    }
