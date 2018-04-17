    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("CopyFile : Source[" + sourceFile.getAbsolutePath() + "] Dest[" + destFile.getAbsolutePath() + "]");
        }
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }
