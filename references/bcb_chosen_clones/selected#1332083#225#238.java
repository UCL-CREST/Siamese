    private void copyJar(File src, File dst) throws IOException {
        FileChannel srcChannel = null;
        FileChannel dstChannel = null;
        try {
            srcChannel = new FileInputStream(src).getChannel();
            dstChannel = new FileOutputStream(dst).getChannel();
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
        } catch (IOException e) {
            fLog.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            srcChannel.close();
            dstChannel.close();
        }
    }
