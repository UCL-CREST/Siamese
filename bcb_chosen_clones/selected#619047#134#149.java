    public static void cleanDir(File f, Logger logger) {
        if (!f.exists()) {
            logger.error("Can not clean. File does not exist = " + f);
            return;
        }
        logger.debug("Deleting contents of " + f);
        File[] files = f.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                cleanDir(files[i], logger);
            }
            if (!files[i].delete()) {
                logger.error("Could not delete file " + files[i].getPath());
            }
        }
    }
