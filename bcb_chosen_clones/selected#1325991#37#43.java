    public static void copyFile(final File in, final File out) throws IOException {
        final FileChannel sourceChannel = new FileInputStream(in).getChannel();
        final FileChannel destinationChannel = new FileOutputStream(out).getChannel();
        sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
        sourceChannel.close();
        destinationChannel.close();
    }
