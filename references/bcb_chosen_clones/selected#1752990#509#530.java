    private void cleanupStagingEnvironment(File stagingDir) throws FileUpdateException {
        if (stagingDir != null && stagingDir.exists() && stagingDir.isDirectory()) {
            File[] files = stagingDir.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                if (!f.delete()) {
                    logger.error("Could not delete: " + f.getAbsolutePath());
                    throw new SessionEnvironmentException("Could not delete: " + f.getAbsolutePath());
                }
            }
            if (stagingDir.exists()) {
                if (stagingDir.delete()) {
                    logger.debug(stagingDir + " was successfully deleted");
                } else {
                    logger.error("Could not clean up staging directory: " + stagingDir.getAbsolutePath());
                    throw new SessionEnvironmentException("Could not remove directory: " + stagingDir.getAbsolutePath());
                }
            }
        } else {
            throw new SessionEnvironmentException("Staging directory undefined, does not exist, or is not a directory");
        }
    }
