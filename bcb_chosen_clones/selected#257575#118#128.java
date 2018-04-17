    private void cleanDir(File dir) {
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                cleanDir(files[i]);
            } else {
                files[i].delete();
            }
        }
        dir.delete();
    }
