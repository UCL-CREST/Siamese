    public void saveFile(final InputStream inputStream, final String fileName) {
        final File file = new File(System.getProperty("java.io.tmpdir") + File.separator + fileName);
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(file);
            IOUtils.copy(inputStream, fileOut);
        } catch (FileNotFoundException e) {
            LOGGER.error("saveFile() - File Not Found." + e);
        } catch (IOException e) {
            LOGGER.error("saveFile() - Error while saving file." + e);
        } finally {
            try {
                inputStream.close();
                if (fileOut != null) {
                    fileOut.close();
                }
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }
    }
