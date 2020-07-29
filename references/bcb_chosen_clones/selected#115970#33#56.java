    private void copyFile(final String sourceFileName, final File path) throws IOException {
        final File source = new File(sourceFileName);
        final File destination = new File(path, source.getName());
        FileChannel srcChannel = null;
        FileChannel dstChannel = null;
        try {
            srcChannel = new FileInputStream(source).getChannel();
            dstChannel = new FileOutputStream(destination).getChannel();
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
        } finally {
            try {
                if (dstChannel != null) {
                    dstChannel.close();
                }
            } catch (Exception exception) {
            }
            try {
                if (srcChannel != null) {
                    srcChannel.close();
                }
            } catch (Exception exception) {
            }
        }
    }
