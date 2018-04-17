    private static void recoursiveDelete(File directory) {
        File[] dirs = directory.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        for (int i = 0; i < dirs.length; i++) {
            recoursiveDelete(dirs[i]);
            dirs[i].delete();
        }
    }
