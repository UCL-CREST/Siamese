    public static void concatFiles(List<File> sourceFiles, File destFile) throws IOException {
        FileOutputStream outFile = new FileOutputStream(destFile);
        FileChannel outChannel = outFile.getChannel();
        for (File f : sourceFiles) {
            FileInputStream fis = new FileInputStream(f);
            FileChannel channel = fis.getChannel();
            channel.transferTo(0, channel.size(), outChannel);
            channel.close();
            fis.close();
        }
        outChannel.close();
    }
