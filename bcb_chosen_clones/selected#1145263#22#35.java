    public static int copyDirectory(File srcDir, File destDir) {
        if (!srcDir.isDirectory() || !destDir.isDirectory()) return -1;
        if (!destDir.exists()) destDir.mkdir();
        File[] srcFiles = srcDir.listFiles();
        for (int i = 0; i < srcFiles.length; i++) {
            File elementCopy = new File(destDir, srcFiles[i].getName());
            if (!srcFiles[i].isDirectory()) {
                if (copyFile(srcFiles[i], elementCopy) != 0) return -2;
            } else {
                FileUtilClass.copyDirectory(srcFiles[i], elementCopy);
            }
        }
        return 0;
    }
