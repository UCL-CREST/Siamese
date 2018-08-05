    public static void deleteAllAt(String directoryName) {
        File directory = new File(directoryName);
        File[] listFiles = directory.listFiles();
        if (listFiles != null && listFiles.length > 0) {
            for (File file : listFiles) {
                if (file.isDirectory()) {
                    deleteAllAt(file.getAbsolutePath());
                } else if (file.isFile()) {
                    deleteFile(file);
                }
            }
        }
        deleteFile(directory);
    }
