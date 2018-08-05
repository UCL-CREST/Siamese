    public static void main(String[] args) throws IOException {
        String uri = "hdfs://localhost:8020/user/leeing/maxtemp/sample.txt";
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        FSDataInputStream in = null;
        try {
            in = fs.open(new Path(uri));
            IOUtils.copyBytes(in, System.out, 8192, false);
            System.out.println("\n");
            in.seek(0);
            IOUtils.copyBytes(in, System.out, 8192, false);
        } finally {
            IOUtils.closeStream(in);
        }
    }
