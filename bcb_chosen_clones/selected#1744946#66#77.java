    private void delete(String path) {
        File fileOrDirectory = new File(path);
        if (fileOrDirectory.isDirectory()) {
            File files[] = fileOrDirectory.listFiles();
            for (File file : files) {
                delete(file.getAbsolutePath());
            }
            fileOrDirectory.delete();
        } else {
            fileOrDirectory.delete();
        }
    }
