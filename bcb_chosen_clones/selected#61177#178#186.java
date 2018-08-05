    private void deleteTree(File file) throws Exception {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                deleteTree(f);
            }
        }
        file.delete();
    }
