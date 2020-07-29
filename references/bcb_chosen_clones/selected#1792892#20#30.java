    public static void copyDirectory(File srcDir, File dstDir, FilenameFilter filter) {
        if (srcDir.canRead() && srcDir.isDirectory()) {
            if (!dstDir.exists()) {
                if (!dstDir.mkdir()) return;
            }
            File[] children = srcDir.listFiles(filter);
            for (int i = 0; i < children.length; i++) {
                if (children[i].isDirectory()) copyDirectory(children[i], new File(dstDir, children[i].getName()), filter); else copyFile(children[i], new File(dstDir, children[i].getName()));
            }
        }
    }
