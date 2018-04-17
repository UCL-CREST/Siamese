    public static void removeFilesUnderDir(final File dir) {
        if (!dir.exists()) {
            return;
        }
        File[] files = dir.listFiles();
        File file;
        for (int i = 0, len = files.length; i < len; i++) {
            file = files[i];
            if (file.isDirectory()) {
                removeFilesUnderDir(file);
                file.delete();
            } else {
                file.delete();
            }
        }
    }
