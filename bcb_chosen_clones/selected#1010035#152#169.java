    public static void copy(final File src, final File target, final java.io.FileFilter fileFilter) throws IOException {
        if (src.isFile()) {
            File dest;
            if (target.exists() && target.isDirectory()) {
                dest = new File(target.getAbsolutePath() + File.separator + target.getName());
            } else {
                dest = target;
            }
            copyFile(src, dest);
        } else if (src.isDirectory()) {
            if (!target.exists()) target.mkdirs();
            for (File f : src.listFiles(fileFilter)) {
                copy(f, new File(target.getAbsolutePath() + File.separator + f.getName()), fileFilter);
            }
        } else {
            throw new IOException(src.getAbsolutePath() + " is neither file nor directory.");
        }
    }
