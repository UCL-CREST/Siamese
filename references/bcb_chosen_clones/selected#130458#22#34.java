    public static void copy(File src, File dst) throws IOException {
        FileChannel srcChannel = new FileInputStream(src).getChannel();
        FileChannel dstChannel = new FileOutputStream(dst).getChannel();
        try {
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
        } finally {
            try {
                srcChannel.close();
            } finally {
                dstChannel.close();
            }
        }
    }
