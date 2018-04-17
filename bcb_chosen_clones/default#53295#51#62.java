    public static void deleteDirectory(File fileDir) {
        File[] fileList = fileDir.listFiles();
        for (int i = 0; i < fileList.length; ++i) {
            if (fileList[i].isDirectory()) {
                deleteDirectory(fileList[i].getAbsoluteFile());
                fileList[i].delete();
            }
            if (fileList[i].isFile()) {
                fileList[i].delete();
            }
        }
    }
