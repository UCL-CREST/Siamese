    public static boolean copyDir(File src, File target) throws IOException {
        if (src == null || target == null || !src.exists()) return false;
        if (!src.isDirectory()) throw new IOException(src.getAbsolutePath() + " should be a directory!");
        if (!target.exists()) if (!makeDir(target)) return false;
        boolean re = true;
        File[] files = src.listFiles();
        if (null != files) {
            for (File f : files) {
                if (f.isFile()) re &= copyFile(f, new File(target.getAbsolutePath() + "/" + f.getName())); else re &= copyDir(f, new File(target.getAbsolutePath() + "/" + f.getName()));
            }
        }
        return re;
    }
