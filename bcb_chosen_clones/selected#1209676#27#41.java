    public static boolean copyFile(final String src, final String dest) {
        if (fileExists(src)) {
            try {
                FileChannel srcChannel = new FileInputStream(src).getChannel();
                FileChannel dstChannel = new FileOutputStream(dest).getChannel();
                dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
                srcChannel.close();
                dstChannel.close();
                return true;
            } catch (IOException e) {
                Logger.getAnonymousLogger().severe(e.getLocalizedMessage());
            }
        }
        return false;
    }
