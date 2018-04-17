    private void copy(File srouceFile, File destinationFile) throws IOException {
        FileChannel sourceChannel = new FileInputStream(srouceFile).getChannel();
        FileChannel destinationChannel = new FileOutputStream(destinationFile).getChannel();
        destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        sourceChannel.close();
        destinationChannel.close();
    }
