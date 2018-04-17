    public static void cleanUpTempFiles() {
        try {
            File tempDirectory = new File(tempDirectoryPath);
            File[] files = tempDirectory.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) deleteDirectory(files[i]);
                    files[i].delete();
                }
            }
            tempDirectory.delete();
        } catch (Exception e) {
        }
    }
