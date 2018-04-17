    private void copyOneFile(String oldPath, String newPath) {
        File copiedFile = new File(newPath);
        try {
            FileInputStream source = new FileInputStream(oldPath);
            FileOutputStream destination = new FileOutputStream(copiedFile);
            FileChannel sourceFileChannel = source.getChannel();
            FileChannel destinationFileChannel = destination.getChannel();
            long size = sourceFileChannel.size();
            sourceFileChannel.transferTo(0, size, destinationFileChannel);
            source.close();
            destination.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
