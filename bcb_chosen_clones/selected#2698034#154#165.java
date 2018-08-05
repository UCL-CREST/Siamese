    public void deleteSub(File path) throws Exception {
        if (!path.isDirectory()) {
            path.delete();
        } else {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; ++i) {
                if (files[i].isDirectory()) deleteSub(files[i]);
                files[i].delete();
            }
            path.delete();
        }
    }
