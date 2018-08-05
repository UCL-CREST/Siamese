    protected void copyFile(final String sourceFileName, final File path) throws IOException {
        final File source = new File(sourceFileName);
        final File destination = new File(path, source.getName());
        FileChannel srcChannel = null;
        FileChannel dstChannel = null;
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(source);
            srcChannel = fileInputStream.getChannel();
            fileOutputStream = new FileOutputStream(destination);
            dstChannel = fileOutputStream.getChannel();
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
            try {
                fileInputStream.close();
            } catch (Exception exception) {
            }
            try {
                fileOutputStream.close();
            } catch (Exception exception) {
            }
        }
    }
