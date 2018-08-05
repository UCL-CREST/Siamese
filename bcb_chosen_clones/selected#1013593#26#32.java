    public static void copy(File sourceFile, File destinationFile) throws IOException {
        FileChannel sourceFileChannel = (new FileInputStream(sourceFile)).getChannel();
        FileChannel destinationFileChannel = (new FileOutputStream(destinationFile)).getChannel();
        sourceFileChannel.transferTo(0, sourceFile.length(), destinationFileChannel);
        sourceFileChannel.close();
        destinationFileChannel.close();
    }
