    protected void clearDirectory(String path, File[] unquenchablesFiles) {
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    clearDirectory(files[i].getAbsolutePath(), unquenchablesFiles);
                }
                if (!isUnquenchable(files[i], unquenchablesFiles)) {
                    files[i].delete();
                }
            }
        }
    }
