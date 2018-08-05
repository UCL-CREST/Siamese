    public void copyDirectory(File srcDir, File dstDir, FileComparator comparator) throws IOException {
        log.debug("copyDirectory");
        if (srcDir.isDirectory()) {
            if (!dstDir.exists()) {
                dstDir.mkdir();
            }
            String[] children = srcDir.list(filter);
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(srcDir, children[i]), new File(dstDir, children[i]), comparator);
            }
        } else {
            if (!comparator.areEqual(srcDir, dstDir)) {
                log.debug("Copying files");
                copyFile(srcDir, dstDir);
            } else {
                log.debug("Source and destination are equal");
            }
        }
        if (srcDir.isDirectory()) {
            log.debug("Finding deleted files");
            File[] srcChildren = srcDir.listFiles();
            Set<String> srcFiles = new HashSet<String>();
            for (int i = 0; i < srcChildren.length; i++) {
                srcFiles.add(srcChildren[i].getName());
            }
            File[] dstChildren = dstDir.listFiles(filter);
            for (int i = 0; i < dstChildren.length; i++) {
                if (!srcFiles.contains(dstChildren[i].getName())) {
                    log.debug("Deleting file: " + dstChildren[i].getAbsolutePath());
                    deleteAll(dstChildren[i]);
                }
            }
        }
    }
