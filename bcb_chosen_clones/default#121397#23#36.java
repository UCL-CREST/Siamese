    public static boolean deleteDir(File dir) {
        if (dir.exists()) {
            File[] contents = dir.listFiles();
            for (int i = 0; i < contents.length; i++) {
                File f = contents[i];
                if (f.isDirectory()) {
                    deleteDir(f);
                } else {
                    f.delete();
                }
            }
        }
        return dir.delete();
    }
