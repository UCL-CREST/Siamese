    public static boolean copy(FileSystem srcFS, Path src, File dst, boolean deleteSource, Configuration conf) throws IOException {
        if (srcFS.getFileStatus(src).isDir()) {
            if (!dst.mkdirs()) {
                return false;
            }
            FileStatus contents[] = srcFS.listStatus(src);
            for (int i = 0; i < contents.length; i++) {
                copy(srcFS, contents[i].getPath(), new File(dst, contents[i].getPath().getName()), deleteSource, conf);
            }
        } else if (srcFS.isFile(src)) {
            InputStream in = srcFS.open(src);
            IOUtils.copyBytes(in, new FileOutputStream(dst), conf);
        } else {
            throw new IOException(src.toString() + ": No such file or directory");
        }
        if (deleteSource) {
            return srcFS.delete(src, true);
        } else {
            return true;
        }
    }
