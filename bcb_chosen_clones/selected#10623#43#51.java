    public static void deleteContentsOfDirectory(File f) {
        File l[] = f.listFiles();
        for (int i = 0; i < l.length; i++) {
            if (l[i].isDirectory()) {
                deleteContentsOfDirectory(l[i]);
                l[i].delete();
            } else l[i].delete();
        }
    }
