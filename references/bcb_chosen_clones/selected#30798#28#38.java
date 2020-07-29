    private void deleteDir(File path) {
        if (path.isDirectory()) {
            File[] children = path.listFiles();
            for (int i = 0, m = children.length; i < m; i++) {
                deleteDir(children[i]);
            }
            path.delete();
        } else {
            path.delete();
        }
    }
