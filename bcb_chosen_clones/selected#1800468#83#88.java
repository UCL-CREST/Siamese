    protected void recursiveDelete(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) recursiveDelete(f);
        }
        file.delete();
    }
