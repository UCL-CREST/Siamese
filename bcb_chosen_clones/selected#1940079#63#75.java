    protected void copy(final File srcDir, final File dstDir) throws IOException {
        if (srcDir.isDirectory()) {
            if (!dstDir.exists()) {
                dstDir.mkdir();
            }
            final String children[] = srcDir.list();
            for (int i = 0; i < children.length; i++) {
                copy(new File(srcDir, children[i]), new File(dstDir, children[i]));
            }
        } else {
            copyFile(srcDir, dstDir);
        }
    }
