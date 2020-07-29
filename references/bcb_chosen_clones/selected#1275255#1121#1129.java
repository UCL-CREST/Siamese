    private void delete(File file) {
        if (file.isDirectory()) {
            File files[] = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                delete(files[i]);
            }
        }
        file.delete();
    }
