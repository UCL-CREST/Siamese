    protected static void cleanFiles(File dir, boolean deleteDirs) {
        if (dir == null) return;
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                cleanFiles(files[i], deleteDirs);
            } else {
                files[i].delete();
            }
        }
        if (deleteDirs) dir.delete();
    }
