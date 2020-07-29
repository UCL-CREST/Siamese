    private static void clean(File dir) {
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    clean(file);
                }
                file.delete();
            }
        }
    }
