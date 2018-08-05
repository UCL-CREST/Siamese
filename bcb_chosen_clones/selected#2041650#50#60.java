    public static void copyFile(File in, File out) throws EnhancedException {
        try {
            FileChannel sourceChannel = new FileInputStream(in).getChannel();
            FileChannel destinationChannel = new FileOutputStream(out).getChannel();
            sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
            sourceChannel.close();
            destinationChannel.close();
        } catch (Exception e) {
            throw new EnhancedException("Could not copy file " + in.getAbsolutePath() + " to " + out.getAbsolutePath() + ".", e);
        }
    }
