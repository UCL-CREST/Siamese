    public static void copyFile(String fileName, String dstPath) throws IOException {
        FileChannel sourceChannel = new FileInputStream(fileName).getChannel();
        FileChannel destinationChannel = new FileOutputStream(dstPath).getChannel();
        sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
        sourceChannel.close();
        destinationChannel.close();
    }
