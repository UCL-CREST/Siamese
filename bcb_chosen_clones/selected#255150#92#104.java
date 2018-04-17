    public static void deltree(File directory) {
        if (directory.exists() && directory.isDirectory()) {
            File[] fileArray = directory.listFiles();
            for (int i = 0; i < fileArray.length; i++) {
                if (fileArray[i].isDirectory()) {
                    deltree(fileArray[i]);
                } else {
                    fileArray[i].delete();
                }
            }
            directory.delete();
        }
    }
