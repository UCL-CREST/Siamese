    public static boolean copyDir(File srcDir, File dstDir) throws IOException {
        if (srcDir.isDirectory()) {
            log.debug(srcDir.getAbsolutePath() + " is directory");
            if (!dstDir.exists()) {
                log.debug(dstDir.getAbsolutePath() + " does not exist, so create it");
                if (!dstDir.mkdirs()) {
                    throw new IOException("Can't create " + dstDir.getName());
                }
            }
            String[] files = srcDir.list();
            log.debug("Copy " + files.length + " files.");
            for (int i = 0; i < files.length; i++) {
                if (copyDir(new File(srcDir, files[i]), new File(dstDir, files[i])) == false) {
                    return false;
                }
            }
            return true;
        }
        log.debug(srcDir.getAbsolutePath() + " is NOT directory");
        return copyFile(srcDir, dstDir);
    }
