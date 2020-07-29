    private void destroyFile(File path) {
        if (path.isDirectory()) {
            File[] tab = path.listFiles();
            for (int i = 0; i < tab.length; i++) {
                destroyFile(tab[i]);
            }
            path.delete();
        } else {
            path.delete();
        }
    }
