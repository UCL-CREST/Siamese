    private void deleteDirectory(File directory) {
        File[] children = directory.listFiles();
        for (int i = 0; i < children.length; i++) {
            if (children[i].isDirectory()) {
                deleteDirectory(children[i]);
            } else {
                children[i].delete();
            }
        }
        directory.delete();
    }
