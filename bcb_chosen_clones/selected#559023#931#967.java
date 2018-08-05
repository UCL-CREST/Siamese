    private void tail(String[] cmd, int pos) throws IOException {
        CommandFormat c = new CommandFormat("tail", 1, 1, "f");
        String src = null;
        Path path = null;
        try {
            List<String> parameters = c.parse(cmd, pos);
            src = parameters.get(0);
        } catch (IllegalArgumentException iae) {
            System.err.println("Usage: java FsShell " + TAIL_USAGE);
            throw iae;
        }
        boolean foption = c.getOpt("f") ? true : false;
        path = new Path(src);
        FileSystem srcFs = path.getFileSystem(getConf());
        if (srcFs.isDirectory(path)) {
            throw new IOException("Source must be a file.");
        }
        long fileSize = srcFs.getFileStatus(path).getLen();
        long offset = (fileSize > 1024) ? fileSize - 1024 : 0;
        while (true) {
            FSDataInputStream in = srcFs.open(path);
            in.seek(offset);
            IOUtils.copyBytes(in, System.out, 1024, false);
            offset = in.getPos();
            in.close();
            if (!foption) {
                break;
            }
            fileSize = srcFs.getFileStatus(path).getLen();
            offset = (fileSize > offset) ? offset : fileSize;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
