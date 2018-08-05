    public static void copyFile(File src, File dst) throws IOException {
        LogUtil.d(TAG, "Copying file %s to %s", src, dst);
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dst).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            closeSafe(inChannel);
            closeSafe(outChannel);
        }
    }
