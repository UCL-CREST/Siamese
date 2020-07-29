    public void copyDirectory(final File sourceDir, final File destinationDir, final FileFilter fileFilter) throws IOException {
        if (sourceDir == null || !sourceDir.exists() || !sourceDir.isDirectory()) {
            final IOException ex = new IOException("Source directory is null, does not exist or is not a directory.");
            LOGGER.error("FileOperations.copyDirectory(): " + ex.getMessage(), ex);
            throw ex;
        }
        if (destinationDir == null || (destinationDir.exists() && !destinationDir.isDirectory())) {
            final IOException ex = new IOException("Destination direcotry is null, does not exist or is not a directory.");
            LOGGER.error("FileOperations.copyDirectory(): " + ex.getMessage(), ex);
            throw ex;
        }
        if (!destinationDir.exists() && !destinationDir.mkdirs()) {
            final IOException ex = new IOException("Unable to create directory: " + destinationDir.getAbsolutePath());
            LOGGER.error("FileOperations.copyDirectory(...): " + ex.getMessage(), ex);
            throw ex;
        }
        final File[] files = sourceDir.listFiles();
        for (final File file : files) {
            if (!fileFilter.accept(file)) {
                continue;
            }
            if (file.isDirectory()) {
                final File destination = new File(destinationDir, file.getName());
                copyDirectory(file, destination, fileFilter);
            } else {
                copyFile(file, new File(destinationDir, file.getName()));
            }
        }
    }
