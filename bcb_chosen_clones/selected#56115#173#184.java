    private void cleanup(File file) throws Exception {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            int size = files.length;
            for (int i = 0; i < size; i++) {
                cleanup(files[i]);
            }
            file.delete();
        } else if (forceDestroy) {
            file.delete();
        }
    }
