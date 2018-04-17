    public static void copyDir(File srcDir, File dstDir) throws IOException {
        if (!dstDir.exists()) {
            dstDir.mkdirs();
        }
        File[] fileList = srcDir.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                copyDir(fileList[i], new File(dstDir, fileList[i].getName()));
            } else {
                copyFile(fileList[i], new File(dstDir, fileList[i].getName()));
            }
        }
    }
