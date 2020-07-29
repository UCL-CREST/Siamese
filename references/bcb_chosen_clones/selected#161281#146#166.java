    public File uploadImage(InputStream inputStream, String fileName, String sessionId) {
        File file = new File(PathConfig.getInstance().sessionFolder(sessionId) + File.separator + fileName);
        FileOutputStream fileOutputStream = null;
        try {
            FileUtils.touch(file);
            fileOutputStream = new FileOutputStream(file);
            IOUtils.copy(inputStream, fileOutputStream);
        } catch (IOException e) {
            logger.error("Save uploaded image to file occur IOException.", e);
            throw new FileOperationException("Save uploaded image to file occur IOException.", e);
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                logger.error("Close FileOutputStream Occur IOException while save a uploaded image.", e);
            }
        }
        return file;
    }
