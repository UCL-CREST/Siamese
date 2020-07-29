    public static void copy(File source, File destination) throws FileNotFoundException, IOException {
        if (source == null) throw new NullPointerException("The source may not be null.");
        if (destination == null) throw new NullPointerException("The destination may not be null.");
        FileInputStream sourceStream = new FileInputStream(source);
        destination.getParentFile().mkdirs();
        FileOutputStream destStream = new FileOutputStream(destination);
        try {
            FileChannel sourceChannel = sourceStream.getChannel();
            FileChannel destChannel = destStream.getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } finally {
            try {
                sourceStream.close();
                destStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
