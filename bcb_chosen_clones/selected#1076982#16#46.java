    public int run(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Download dir local");
            return 1;
        }
        OutputStream out = new FileOutputStream(args[1]);
        Path srcDir = new Path(args[0]);
        Configuration conf = new Configuration();
        FileSystem srcFS = FileSystem.get(conf);
        if (!srcFS.getFileStatus(srcDir).isDirectory()) {
            System.err.println(args[0] + " is not a directory.");
            return 1;
        }
        try {
            FileStatus contents[] = srcFS.listStatus(srcDir);
            for (int i = 0; i < contents.length; i++) {
                if (contents[i].isFile()) {
                    System.err.println(contents[i].getPath());
                    InputStream in = srcFS.open(contents[i].getPath());
                    try {
                        IOUtils.copyBytes(in, out, conf, false);
                    } finally {
                        in.close();
                    }
                }
            }
        } finally {
            out.close();
        }
        return 0;
    }
