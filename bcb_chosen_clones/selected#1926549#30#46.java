    public void run(String srcf, String dst) {
        final Path srcPath = new Path("./" + srcf);
        final Path desPath = new Path(dst);
        try {
            Path[] srcs = FileUtil.stat2Paths(hdfs.globStatus(srcPath), srcPath);
            OutputStream out = FileSystem.getLocal(conf).create(desPath);
            for (int i = 0; i < srcs.length; i++) {
                System.out.println(srcs[i]);
                InputStream in = hdfs.open(srcs[i]);
                IOUtils.copyBytes(in, out, conf, false);
                in.close();
            }
            out.close();
        } catch (IOException ex) {
            System.err.print(ex.getMessage());
        }
    }
