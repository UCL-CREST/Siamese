    public static boolean copyDir(File srcFile, File destFile, boolean overwrite) {
        logger.debug("Copy srcFile[" + srcFile.getAbsolutePath() + "] to destFile[" + destFile.getAbsolutePath() + "] and" + " overwrite file if present");
        boolean result = false;
        if (srcFile.isDirectory()) {
            if (!destFile.isDirectory() && !destFile.mkdirs()) {
                return false;
            }
            File[] listFiles = srcFile.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                File curFile = listFiles[i];
                if (curFile.isDirectory()) {
                    result = copyDir(curFile, new File(destFile, curFile.getName()), overwrite);
                } else {
                    result = copyFile(curFile, new File(destFile, curFile.getName()), overwrite);
                }
                if (result == false) {
                    return result;
                }
            }
        }
        return result;
    }
