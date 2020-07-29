    public static void copyFile(File from, File to) throws FileNotFoundException, IOException {
        requireFile(from);
        requireFile(to);
        if (to.isDirectory()) {
            to = new File(to, getFileName(from));
        }
        FileChannel sourceChannel = new FileInputStream(from).getChannel();
        FileChannel destinationChannel = new FileOutputStream(to).getChannel();
        destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        sourceChannel.close();
        destinationChannel.close();
    }
