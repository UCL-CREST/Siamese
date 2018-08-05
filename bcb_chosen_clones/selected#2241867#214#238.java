    protected static boolean deleteFile(final File file, final int maxRetryCount, final int retryCount) {
        if (file == null || !file.exists()) {
            return Boolean.FALSE;
        }
        if (file.isDirectory()) {
            File children[] = file.listFiles();
            for (int j = 0; j < children.length; j++) {
                File child = children[j];
                deleteFile(child, maxRetryCount, 0);
            }
        }
        if (file.delete()) {
            return Boolean.TRUE;
        }
        if (retryCount >= maxRetryCount) {
            if (file.exists()) {
                LOGGER.debug("Couldn't delete file : " + file);
                LOGGER.debug("Will try to delete on exit : ");
                file.deleteOnExit();
            }
            return Boolean.FALSE;
        }
        LOGGER.debug("Retrying count : " + retryCount + ", file : " + file);
        return deleteFile(file, maxRetryCount, retryCount + 1);
    }
