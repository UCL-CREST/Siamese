    public static void copy(FileInputStream from, FileOutputStream to) throws IOException {
        FileChannel fromChannel = from.getChannel();
        FileChannel toChannel = to.getChannel();
        copy(fromChannel, toChannel);
        fromChannel.close();
        toChannel.close();
    }
