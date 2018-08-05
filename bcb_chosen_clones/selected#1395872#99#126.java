    public static boolean copyFile(final File src, final File dst) {
        boolean result = false;
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        synchronized (FileUtil.DATA_LOCK) {
            try {
                inChannel = new FileInputStream(src).getChannel();
                outChannel = new FileOutputStream(dst).getChannel();
                inChannel.transferTo(0, inChannel.size(), outChannel);
                result = true;
            } catch (IOException e) {
            } finally {
                if (inChannel != null && inChannel.isOpen()) {
                    try {
                        inChannel.close();
                    } catch (IOException e) {
                    }
                }
                if (outChannel != null && outChannel.isOpen()) {
                    try {
                        outChannel.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
        return result;
    }
