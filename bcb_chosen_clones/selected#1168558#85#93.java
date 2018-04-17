    private void deleteOnExit(File tempFile) {
        tempFile.deleteOnExit();
        if (tempFile.isDirectory()) {
            File[] files = tempFile.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteOnExit(files[i]);
            }
        }
    }
