    public static void copyFile(String input, String output) {
        try {
            FileChannel srcChannel = new FileInputStream("srcFilename").getChannel();
            FileChannel dstChannel = new FileOutputStream("dstFilename").getChannel();
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
            srcChannel.close();
            dstChannel.close();
        } catch (IOException e) {
        }
    }
