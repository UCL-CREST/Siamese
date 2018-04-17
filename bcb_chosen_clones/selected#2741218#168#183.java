    protected void copyFile(String inputFilePath, String outputFilePath) throws GenerationException {
        String from = getTemplateDir() + inputFilePath;
        try {
            logger.debug("Copying from " + from + " to " + outputFilePath);
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(from);
            if (inputStream == null) {
                throw new GenerationException("Source file not found: " + from);
            }
            FileOutputStream outputStream = new FileOutputStream(new File(outputFilePath));
            IOUtils.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            throw new GenerationException("Error while copying file: " + from, e);
        }
    }
