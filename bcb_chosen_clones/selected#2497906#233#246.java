    private void mergeOne(String level, char strand, String filename, Path outPath, FileSystem srcFS, FileSystem dstFS, Timer to) throws IOException {
        to.start();
        final OutputStream outs = dstFS.create(new Path(outPath, filename));
        final FileStatus[] parts = srcFS.globStatus(new Path(sorted ? getSortOutputDir(level, strand) : wrkDir, filename + "-[0-9][0-9][0-9][0-9][0-9][0-9]"));
        for (final FileStatus part : parts) {
            final InputStream ins = srcFS.open(part.getPath());
            IOUtils.copyBytes(ins, outs, getConf(), false);
            ins.close();
        }
        for (final FileStatus part : parts) srcFS.delete(part.getPath(), false);
        outs.write(BlockCompressedStreamConstants.EMPTY_GZIP_BLOCK);
        outs.close();
        System.out.printf("summarize :: Merged %s%c in %d.%03d s.\n", level, strand, to.stopS(), to.fms());
    }
