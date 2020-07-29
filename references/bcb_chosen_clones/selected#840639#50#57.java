    public static void recursiveDelete(File transmissionFolder) {
        if (transmissionFolder.isDirectory()) {
            for (File file : transmissionFolder.listFiles()) {
                recursiveDelete(file);
            }
        }
        transmissionFolder.delete();
    }
