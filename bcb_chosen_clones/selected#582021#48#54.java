    public static void copyFile(File inputFile, File outputFile) throws IOException {
        FileChannel srcChannel = new FileInputStream(inputFile).getChannel();
        FileChannel dstChannel = new FileOutputStream(outputFile).getChannel();
        dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
        srcChannel.close();
        dstChannel.close();
    }
