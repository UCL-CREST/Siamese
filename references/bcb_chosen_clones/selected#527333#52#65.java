    public static void deleteDir(File path) throws Exception {
        if (path != null) {
            File[] files = path.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; ++i) {
                    if (files[i].isDirectory()) {
                        deleteDir(files[i]);
                    }
                    files[i].delete();
                }
            }
            path.delete();
        }
    }
