    private static void deleteDir(final File dir) {
        File[] outDirContents = dir.listFiles();
        for (int i = 0; i < outDirContents.length; i++) {
            if (isLink(outDirContents[i])) {
                continue;
            }
            if (outDirContents[i].isDirectory()) {
                deleteDir(outDirContents[i]);
            }
            if (!outDirContents[i].delete() && outDirContents[i].exists()) {
                log.error("Could not delete " + outDirContents[i].getAbsolutePath());
            }
        }
        if (!dir.delete()) {
            log.error("Could not delete " + dir.getAbsolutePath());
        }
    }
