    private File uploadFile(InputStream inputStream, File file) {
        FileOutputStream fileOutputStream = null;
        try {
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileUtils.touch(file);
            fileOutputStream = new FileOutputStream(file);
            IOUtils.copy(inputStream, fileOutputStream);
        } catch (IOException e) {
            throw new FileOperationException("Failed to save uploaded image", e);
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                LOGGER.warn("Failed to close resources on uploaded file", e);
            }
        }
        return file;
    }
