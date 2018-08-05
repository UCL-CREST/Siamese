    public static void copyFile(FileInputStream source, FileOutputStream target) throws Exception {
        FileChannel inChannel = source.getChannel();
        FileChannel outChannel = target.getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            throw e;
        } finally {
            if (inChannel != null) inChannel.close();
            if (outChannel != null) outChannel.close();
        }
    }
