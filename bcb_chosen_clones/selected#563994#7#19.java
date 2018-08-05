    public void deleteSubFile(File file) {
        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            for (File f : fileList) {
                if (f.isFile()) {
                    f.delete();
                } else {
                    deleteSubFile(f);
                    f.delete();
                }
            }
        }
    }
