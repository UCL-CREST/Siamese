    private void fileCopy(final File src, final File dest) throws IOException {
        final FileChannel srcChannel = new FileInputStream(src).getChannel();
        final FileChannel dstChannel = new FileOutputStream(dest).getChannel();
        dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
        srcChannel.close();
        dstChannel.close();
    }
