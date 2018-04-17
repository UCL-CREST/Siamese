    public static void deleteFile(File dir, FileFilter filter, boolean deleteDirOnEmpty) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) return;
        File[] fs = dir.listFiles(filter);
        for (File f : fs) {
            if (f.exists()) {
                if (f.isDirectory()) {
                    deleteFile(f, filter, deleteDirOnEmpty);
                } else {
                    f.delete();
                }
            }
        }
        if (dir.list().length == 0 && deleteDirOnEmpty) {
            dir.delete();
        }
    }
