    public static void copyDirectory(File src, File dst, boolean recurse) throws IOException {
        dst.mkdirs();
        if (!dst.isDirectory()) {
            throw new IOException("cannot make target directory: " + dst.getAbsolutePath());
        }
        File[] files = src.listFiles();
        for (File f : files) {
            String name = f.getName();
            if (!".svn".equals(name)) {
                if (f.isFile()) {
                    copyFile(f, new File(dst, name));
                } else if (f.isDirectory() && recurse) {
                    copyDirectory(f, new File(dst, name), true);
                }
            }
        }
    }
