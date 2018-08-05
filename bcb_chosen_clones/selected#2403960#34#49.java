    public static void copy(FileInputStream in, File destination) throws IOException {
        FileChannel srcChannel = null;
        FileChannel dstChannel = null;
        try {
            srcChannel = in.getChannel();
            dstChannel = new FileOutputStream(destination).getChannel();
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
        } finally {
            if (srcChannel != null) {
                srcChannel.close();
            }
            if (dstChannel != null) {
                dstChannel.close();
            }
        }
    }
