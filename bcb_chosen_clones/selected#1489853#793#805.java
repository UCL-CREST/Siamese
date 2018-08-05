    public static void copy(File src, File dst) {
        try {
            FileChannel srcChannel = new FileInputStream(src).getChannel();
            FileChannel dstChannel = new FileOutputStream(dst).getChannel();
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
            srcChannel.close();
            srcChannel = null;
            dstChannel.close();
            dstChannel = null;
        } catch (IOException ex) {
            Tools.logException(Tools.class, ex, dst.getAbsolutePath());
        }
    }
