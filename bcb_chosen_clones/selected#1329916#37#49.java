    private void removeRepository(File file) {
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            file.delete();
            return;
        }
        for (File tmp : file.listFiles()) {
            removeRepository(tmp);
        }
        file.delete();
    }
