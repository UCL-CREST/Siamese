    public static void rmdirRecursive(File file) {
        logger.debug("Deleting file " + file.getName());
        if (!file.exists()) {
            logger.error("Folder " + file.getName() + " doesn't exist.");
            return;
        }
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                rmdirRecursive(f);
            } else {
                if (!f.delete()) {
                    logger.warn("File " + f.getName() + " wasn't deleted.");
                }
            }
        }
        if (!file.delete()) {
            logger.warn("Folder " + file.getName() + " wasn't deleted.");
        }
    }
