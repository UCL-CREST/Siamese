    private static void deleteDir(File dir) {
        if (!dir.isDirectory()) {
            dir.delete();
            return;
        }
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.isDirectory()) deleteDir(f); else f.delete();
        }
        dir.delete();
    }
