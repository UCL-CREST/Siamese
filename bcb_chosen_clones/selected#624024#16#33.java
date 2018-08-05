    public static void copyFile(File in, File out) throws FileNotFoundException, IOException {
        FileChannel sourceChannel = null;
        FileChannel destinationChannel = null;
        try {
            sourceChannel = new FileInputStream(in).getChannel();
            destinationChannel = new FileOutputStream(out).getChannel();
            sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
        } finally {
            try {
                sourceChannel.close();
            } catch (Exception ex) {
            }
            try {
                destinationChannel.close();
            } catch (Exception ex) {
            }
        }
    }
