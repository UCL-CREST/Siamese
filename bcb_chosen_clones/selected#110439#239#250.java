    private void copyMerge(Path[] sources, OutputStream out) throws IOException {
        Configuration conf = getConf();
        for (int i = 0; i < sources.length; ++i) {
            FileSystem fs = sources[i].getFileSystem(conf);
            InputStream in = fs.open(sources[i]);
            try {
                IOUtils.copyBytes(in, out, conf, false);
            } finally {
                in.close();
            }
        }
    }
