    public void clearCache() {
        File dir = new File(localCacheDir);
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (!files[i].delete()) {
                    logger.warn("Failed to delete cached hublist file " + files[i].getName());
                }
            }
        }
    }
