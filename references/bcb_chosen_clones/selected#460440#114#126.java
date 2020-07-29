    public static void transfer(FileInputStream fileInStream, FileOutputStream fileOutStream) throws IOException {
        FileChannel fileInChannel = fileInStream.getChannel();
        FileChannel fileOutChannel = fileOutStream.getChannel();
        long fileInSize = fileInChannel.size();
        try {
            long transferred = fileInChannel.transferTo(0, fileInSize, fileOutChannel);
            if (transferred != fileInSize) {
                throw new IOException("transfer() did not complete");
            }
        } finally {
            ensureClose(fileInChannel, fileOutChannel);
        }
    }
