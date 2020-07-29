    public void copyDirectory(File srcDir, File dstDir) throws IOException {
        log.debug("copyDirectory");
        if (srcDir.isDirectory()) {
            if (!dstDir.exists()) {
                dstDir.mkdir();
            }
            String[] children = srcDir.list(filter);
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(srcDir, children[i]), new File(dstDir, children[i]));
            }
        } else {
            copyFile(srcDir, dstDir);
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
