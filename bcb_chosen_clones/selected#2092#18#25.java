    public static void recursiveDelete(File dir) {
        if (dir.isDirectory()) {
            for (File subfiles : dir.listFiles()) {
                recursiveDelete(subfiles);
            }
        }
        dir.delete();
    }
