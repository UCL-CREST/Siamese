    public static void deleteRecursive(File dir) {
        if (dir.exists()) {
            if (dir.isDirectory()) {
                for (File f : dir.listFiles()) {
                    deleteRecursive(f);
                }
            }
            dir.delete();
        }
    }
