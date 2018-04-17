    public void copyFile(final File sourceFile, final File destinationFile) throws FileIOException {
        final FileChannel sourceChannel;
        try {
            sourceChannel = new FileInputStream(sourceFile).getChannel();
        } catch (FileNotFoundException exception) {
            final String message = COPY_FILE_FAILED + sourceFile + " -> " + destinationFile;
            LOGGER.fatal(message);
            throw fileIOException(message, sourceFile, exception);
        }
        final FileChannel destinationChannel;
        try {
            destinationChannel = new FileOutputStream(destinationFile).getChannel();
        } catch (FileNotFoundException exception) {
            final String message = COPY_FILE_FAILED + sourceFile + " -> " + destinationFile;
            LOGGER.fatal(message);
            throw fileIOException(message, destinationFile, exception);
        }
        try {
            destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } catch (Exception exception) {
            final String message = COPY_FILE_FAILED + sourceFile + " -> " + destinationFile;
            LOGGER.fatal(message);
            throw fileIOException(message, null, exception);
        } finally {
            if (sourceChannel != null) {
                try {
                    sourceChannel.close();
                } catch (IOException exception) {
                    LOGGER.error("closing source", exception);
                }
            }
            if (destinationChannel != null) {
                try {
                    destinationChannel.close();
                } catch (IOException exception) {
                    LOGGER.error("closing destination", exception);
                }
            }
        }
    }
