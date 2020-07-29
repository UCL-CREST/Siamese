    private void saveFile(Folder folder, Object key, InputStream stream) throws FileManagerException {
        File file = new File(folder, key.toString());
        LOGGER.debug("Writing file: " + file.getAbsolutePath());
        Writer writer = null;
        Writer encodedWriter = null;
        try {
            encodedWriter = new OutputStreamWriter(new FileOutputStream(file), getEncodeCharset());
            IOUtils.copy(stream, encodedWriter, getDecodeCharset());
            LOGGER.info("saveFile(), decode charset: " + getDecodeCharset() + ", encode charset: " + getEncodeCharset());
        } catch (IOException e) {
            throw new FileManagerException("Unable to write to file: " + file.getAbsolutePath(), e);
        } finally {
            try {
                encodedWriter.close();
            } catch (IOException e) {
                throw new FileManagerException("Unable to write to file: " + file.getAbsolutePath(), e);
            }
        }
    }
