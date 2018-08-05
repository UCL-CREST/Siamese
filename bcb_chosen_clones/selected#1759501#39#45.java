    private static void deleteDir(File dir) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) deleteDir(file);
            if (!file.delete()) System.err.println("Can't delete file (dir) - " + file.toString());
        }
    }
