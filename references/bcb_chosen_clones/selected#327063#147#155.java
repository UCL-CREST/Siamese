    public static int copyDirectory(File srcDir, File dstDir) throws IOException {
        if (srcDir.isHidden() || !srcDir.exists()) return 0;
        if (srcDir.isDirectory()) {
            if (!dstDir.exists()) dstDir.mkdirs();
            int ret = 0;
            for (String children : srcDir.list()) ret += copyDirectory(new File(srcDir, children), new File(dstDir, children));
            return ret;
        } else return copyFile(srcDir, dstDir);
    }
