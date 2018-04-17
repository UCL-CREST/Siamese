    public static void copyFile(String target, String source) {
        try {
            FileChannel srcChannel = new FileInputStream(source).getChannel();
            FileChannel dstChannel = new FileOutputStream(target).getChannel();
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
            srcChannel.close();
            dstChannel.close();
        } catch (IOException e) {
            ExceptionHelper.showErrorDialog(e);
        }
    }
