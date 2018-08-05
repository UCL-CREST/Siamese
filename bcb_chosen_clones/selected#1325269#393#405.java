    private void copyDirectory(final File srcDir, final File dstDir) throws IOException {
        if (srcDir.isDirectory()) {
            if (!dstDir.exists()) {
                dstDir.mkdir();
            }
            final String[] children = srcDir.list();
            for (final String element : children) {
                this.copyDirectory(new File(srcDir, element), new File(dstDir, element));
            }
        } else {
            this.copyFile(srcDir, dstDir);
        }
    }
