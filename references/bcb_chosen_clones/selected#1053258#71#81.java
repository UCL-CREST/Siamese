    public static void deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] tab = dir.listFiles();
            if (tab != null) {
                for (int i = 0, max = tab.length; i < max; i++) {
                    if (tab[i].isDirectory()) deleteDir(tab[i]); else tab[i].delete();
                }
            }
        }
        dir.delete();
    }
