    public static boolean copyDir(File srcDir, File destDir) {
        if (null == srcDir || !srcDir.exists() || !srcDir.isDirectory()) {
            log.error("srcDir(" + srcDir + ") is invalid !");
            return false;
        }
        if (srcDir.equals(destDir)) {
            log.error("srcDir(" + srcDir + ") is the same as destDir(" + destDir + ") !");
            return false;
        }
        List<File> fileList = listDescendFiles(srcDir, null);
        File path = null;
        for (File file : fileList) {
            path = new File(file.getParent().replace(srcDir.getAbsolutePath(), destDir.getAbsolutePath()));
            if (!copyFile(file, path)) {
                return false;
            }
        }
        return true;
    }
