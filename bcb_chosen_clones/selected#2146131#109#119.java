    public static void removeDir(File dir) throws IOException {
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                FileUtil.removeDir(files[i]);
            } else {
                files[i].delete();
            }
        }
        dir.delete();
    }
