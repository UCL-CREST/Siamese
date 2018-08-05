    public static void delete(File f) {
        if (f != null && f.exists()) {
            if (f.isDirectory()) {
                File[] files = f.listFiles();
                for (int i = 0; i < files.length; i++) {
                    delete(files[i]);
                }
            }
            if (!f.delete()) {
                logger.debug("Cannot delete " + f.getAbsolutePath());
            }
        } else {
            logger.debug(f + " does not exist");
        }
    }
