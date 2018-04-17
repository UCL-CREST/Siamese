    public static void removeTouchFiles(File container) {
        if (container.exists() && container.isDirectory()) for (File file : container.listFiles()) {
            if (file.isDirectory()) {
                removeTouchFiles(file);
            } else if (file.isFile()) {
                if (file.getName().equals(TOUCH_FILE_NAME)) file.delete();
            }
        }
    }
