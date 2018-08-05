    public boolean copyDirectoryToOneDir(File inputDir, File outputDir) {
        if (!inputDir.isDirectory()) return false;
        if (!FileUtil.makeDirectory(outputDir.getAbsolutePath())) {
            return false;
        }
        File[] files = inputDir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                if (!copy(file, new File(outputDir.getAbsolutePath() + File.separator + file.getName()))) {
                    return false;
                }
            } else if (file.isDirectory()) {
                if (!copyDirectoryToOneDir(file, outputDir)) {
                    return false;
                }
            }
        }
        return true;
    }
