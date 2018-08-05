    public static void copyFiles(File sourceDir, File targetDir, FileFilter fileFilter) throws IOException {
        if (!sourceDir.isDirectory()) {
            throw new IllegalArgumentException("The given sourceDir '" + sourceDir + "' must be a directory.");
        }
        if (!sourceDir.isDirectory()) {
            throw new IllegalArgumentException("The given targetDir '" + targetDir + "' must be a directory.");
        }
        File[] sourceChildren = null;
        if (fileFilter == null) {
            sourceChildren = sourceDir.listFiles();
        } else {
            sourceChildren = sourceDir.listFiles(fileFilter);
        }
        if (sourceChildren == null) {
            return;
        }
        List<File> fileList = Arrays.asList(sourceChildren);
        for (Iterator<File> iter = fileList.iterator(); iter.hasNext(); ) {
            File file = iter.next();
            File fileInTarget = new File(targetDir, file.getName());
            if (file.isDirectory()) {
                boolean dirSuccess = fileInTarget.mkdir();
                if (!dirSuccess) {
                    throw new RuntimeException("Could not create directory " + targetDir + ". Aborting");
                }
                log.debug("Created directory '" + fileInTarget + "'");
                FileUtil.copyFiles(file, fileInTarget, fileFilter);
            } else {
                FileUtil.copyFile(file, fileInTarget);
            }
        }
    }
