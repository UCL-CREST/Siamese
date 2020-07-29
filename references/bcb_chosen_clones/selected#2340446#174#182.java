    private void deletFileRecursive(File file) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            for (int i = 0, max = children.length; i < max; i++) {
                deletFileRecursive(children[i]);
            }
        }
        file.delete();
    }
