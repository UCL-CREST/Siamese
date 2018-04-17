    public static boolean copyMerge(FileSystem srcFS, Path srcDir, FileSystem dstFS, Path dstFile, boolean deleteSource, Configuration conf, String addString) throws IOException {
        dstFile = checkDest(srcDir.getName(), dstFS, dstFile, false);
        if (!srcFS.getFileStatus(srcDir).isDir()) return false;
        OutputStream out = dstFS.create(dstFile);
        try {
            FileStatus contents[] = srcFS.listStatus(srcDir);
            for (int i = 0; i < contents.length; i++) {
                if (!contents[i].isDir()) {
                    InputStream in = srcFS.open(contents[i].getPath());
                    try {
                        IOUtils.copyBytes(in, out, conf, false);
                        if (addString != null) out.write(addString.getBytes("UTF-8"));
                    } finally {
                        in.close();
                    }
                }
            }
        } finally {
            out.close();
        }
        if (deleteSource) {
            return srcFS.delete(srcDir, true);
        } else {
            return true;
        }
    }
