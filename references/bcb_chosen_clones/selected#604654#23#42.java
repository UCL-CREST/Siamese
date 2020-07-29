    public static boolean recursiveDeleteDirectory(final File path) {
        LOG.info(">>recursiveDeleteDirectory");
        if (path.exists()) {
            LOG.info("deleting files in directory " + path);
            for (File file : path.listFiles()) {
                LOG.info("looking at file " + file);
                if (file.isDirectory()) {
                    boolean result = recursiveDeleteDirectory(file);
                    if (!result) {
                        return false;
                    }
                } else {
                    LOG.info("delete file " + file);
                    file.delete();
                }
            }
        }
        LOG.info("<<recursiveDeleteDirectory");
        return path.delete();
    }
