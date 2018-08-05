    public static void CopyFile(String in, String out) throws Exception {
        FileChannel sourceChannel = new FileInputStream(new File(in)).getChannel();
        FileChannel destinationChannel = new FileOutputStream(new File(out)).getChannel();
        sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
        sourceChannel.close();
        destinationChannel.close();
    }
