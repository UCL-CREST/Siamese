    public boolean deleteRecursive() {
        if (isDirectory()) {
            java.io.File[] files = listFiles();
            for (int i = 0; i < files.length; i++) {
                File file = new File(files[i]);
                file.deleteRecursive();
            }
        }
        return super.delete();
    }
