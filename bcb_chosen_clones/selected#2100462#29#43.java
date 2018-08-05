    public void deleteDirectory(File dir) {
        if (!dir.exists()) {
            return;
        }
        System.out.println(">> " + dir.getName());
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                deleteDir(files[i]);
            } else {
                files[i].delete();
                return;
            }
        }
    }
