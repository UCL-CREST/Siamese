    public static void copyFile(File in, File out) throws IOException {
        FileChannel sourceChannel = new FileInputStream(in).getChannel();
        FileChannel destinationChannel = new FileOutputStream(out).getChannel();
        try {
            sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
        } finally {
            sourceChannel.close();
            destinationChannel.close();
        }
    }
