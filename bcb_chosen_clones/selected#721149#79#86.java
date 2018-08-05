    protected boolean recursiveFileDelete(File file) {
        if (file.isDirectory()) {
            for (File fChild : file.listFiles()) {
                recursiveFileDelete(fChild);
            }
        }
        return file.delete();
    }
