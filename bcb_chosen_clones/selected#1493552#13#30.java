    public static final boolean deleteDirectory(File directory) {
        if (!directory.exists()) {
            return true;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                if (!deleteDirectory(file)) {
                    return false;
                }
            } else {
                if (!file.delete()) {
                    return false;
                }
            }
        }
        return directory.delete();
    }
