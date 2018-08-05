    public static void deleteFiles(File directory) throws IOException {
        File[] allTestFiles = directory.listFiles();
        for (int i = 0; i < allTestFiles.length; i++) {
            if (allTestFiles[i].isDirectory()) {
                deleteFiles(allTestFiles[i]);
                allTestFiles[i].delete();
            } else {
                allTestFiles[i].delete();
            }
        }
    }
