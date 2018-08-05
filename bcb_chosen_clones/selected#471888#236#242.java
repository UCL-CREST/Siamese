    private static final void copyDir(File dst, File src, int flags) throws IOException {
        assert D.OFF || src.isDirectory();
        final File[] srcs = src.listFiles();
        for (int j = 0; j < srcs.length; ++j) {
            copy(new File(dst, srcs[j].getName()), srcs[j], flags);
        }
    }
