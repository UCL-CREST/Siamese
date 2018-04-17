    private static void deleteDirectory(File f) {
        File[] files = f.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) deleteDirectory(files[i]);
            files[i].delete();
        }
    }
