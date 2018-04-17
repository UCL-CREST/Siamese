    public static void deleteDirectory(File dir) {
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                deleteDirectory(f);
            } else f.delete();
        }
        dir.delete();
    }
