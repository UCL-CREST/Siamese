    public static void del(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                del(child);
            }
        }
        file.delete();
    }
