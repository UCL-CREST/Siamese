    private void deleteDirectory(File dir) {
        File[] a = dir.listFiles();
        if (a != null) {
            for (File f : a) {
                if (f.isDirectory()) {
                    deleteDirectory(f);
                } else {
                    f.delete();
                }
            }
        }
        dir.delete();
    }
