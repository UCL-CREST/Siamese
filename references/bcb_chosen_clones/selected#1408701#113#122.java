    private void deleteDir(File dir) {
        if (dir.isDirectory()) {
            File fileList[] = dir.listFiles();
            for (int index = 0; index < fileList.length; index++) {
                File file = fileList[index];
                deleteDir(file);
            }
        }
        dir.delete();
    }
