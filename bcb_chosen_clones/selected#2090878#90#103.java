    public static boolean recursiveDelete(File dir) throws SecurityException {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; ++i) {
                if (files[i].isDirectory()) {
                    if (!recursiveDelete(files[i])) return false;
                } else {
                    if (!files[i].delete()) return false;
                }
            }
            return dir.delete();
        }
        return false;
    }
