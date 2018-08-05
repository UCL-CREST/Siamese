    @Override
    protected int run(CmdLineParser parser) {
        final List<String> args = parser.getRemainingArgs();
        if (args.isEmpty()) {
            System.err.println("summarysort :: WORKDIR not given.");
            return 3;
        }
        if (args.size() == 1) {
            System.err.println("summarysort :: INPATH not given.");
            return 3;
        }
        final String outS = (String) parser.getOptionValue(outputDirOpt);
        final Path wrkDir = new Path(args.get(0)), in = new Path(args.get(1)), out = outS == null ? null : new Path(outS);
        final boolean verbose = parser.getBoolean(verboseOpt);
        final Configuration conf = getConf();
        final Timer t = new Timer();
        try {
            @SuppressWarnings("deprecation") final int maxReduceTasks = new JobClient(new JobConf(conf)).getClusterStatus().getMaxReduceTasks();
            conf.setInt("mapred.reduce.tasks", Math.max(1, maxReduceTasks * 9 / 10));
            final Job job = sortOne(conf, in, wrkDir, "summarysort", "");
            System.out.printf("summarysort :: Waiting for job completion...\n");
            t.start();
            if (!job.waitForCompletion(verbose)) {
                System.err.println("summarysort :: Job failed.");
                return 4;
            }
            System.out.printf("summarysort :: Job complete in %d.%03d s.\n", t.stopS(), t.fms());
        } catch (IOException e) {
            System.err.printf("summarysort :: Hadoop error: %s\n", e);
            return 4;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (out != null) try {
            System.out.println("summarysort :: Merging output...");
            t.start();
            final FileSystem srcFS = wrkDir.getFileSystem(conf);
            final FileSystem dstFS = out.getFileSystem(conf);
            final OutputStream outs = dstFS.create(out);
            final FileStatus[] parts = srcFS.globStatus(new Path(wrkDir, in.getName() + "-[0-9][0-9][0-9][0-9][0-9][0-9]*"));
            {
                int i = 0;
                final Timer t2 = new Timer();
                for (final FileStatus part : parts) {
                    t2.start();
                    final InputStream ins = srcFS.open(part.getPath());
                    IOUtils.copyBytes(ins, outs, conf, false);
                    ins.close();
                    System.out.printf("summarysort :: Merged part %d in %d.%03d s.\n", ++i, t2.stopS(), t2.fms());
                }
            }
            for (final FileStatus part : parts) srcFS.delete(part.getPath(), false);
            outs.write(BlockCompressedStreamConstants.EMPTY_GZIP_BLOCK);
            outs.close();
            System.out.printf("summarysort :: Merging complete in %d.%03d s.\n", t.stopS(), t.fms());
        } catch (IOException e) {
            System.err.printf("summarysort :: Output merging failed: %s\n", e);
            return 5;
        }
        return 0;
    }
