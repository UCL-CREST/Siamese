    protected void delete(File file) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            for (int i = 0; i < children.length; i++) {
                delete(children[i]);
            }
        }
        if (!file.delete()) {
            file.delete();
        }
    }
