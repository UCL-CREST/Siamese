    private void deleteTree(File path) {
        File files[] = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) deleteTree(files[i]);
            System.out.println("delete " + files[i].getPath());
            files[i].delete();
        }
        path.delete();
    }
