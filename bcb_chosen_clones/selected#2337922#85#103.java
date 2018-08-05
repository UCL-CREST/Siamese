    public static boolean deleteDir(File dir) {
        if (dir == null) return false;
        File candir;
        try {
            candir = dir.getCanonicalFile();
        } catch (IOException e) {
            return false;
        }
        if (!candir.equals(dir.getAbsoluteFile())) return false;
        File files[] = candir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                boolean deleted = !file.delete();
                if (deleted && file.isDirectory()) deleteDir(file);
            }
        }
        return dir.delete();
    }
