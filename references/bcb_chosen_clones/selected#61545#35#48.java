    public static void deleteDirectory(File dir) {
        FileUtils.log.debug("deleting directory ... " + dir.getAbsolutePath());
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        dir.delete();
    }
