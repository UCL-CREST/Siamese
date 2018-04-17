    private void deleteDir(File file) {
        if (file == null) return;
        if (file.isFile()) file.delete();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) deleteDir(files[i]);
            file.delete();
        }
    }
