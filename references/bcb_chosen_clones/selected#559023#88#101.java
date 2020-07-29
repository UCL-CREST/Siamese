    private void copyFromStdin(Path dst, FileSystem dstFs) throws IOException {
        if (dstFs.isDirectory(dst)) {
            throw new IOException("When source is stdin, destination must be a file.");
        }
        if (dstFs.exists(dst)) {
            throw new IOException("Target " + dst.toString() + " already exists.");
        }
        FSDataOutputStream out = dstFs.create(dst);
        try {
            IOUtils.copyBytes(System.in, out, getConf(), false);
        } finally {
            out.close();
        }
    }
