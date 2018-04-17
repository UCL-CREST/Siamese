    public void copyFile(File source, File destination) {
        try {
            FileInputStream sourceStream = new FileInputStream(source);
            try {
                FileOutputStream destinationStream = new FileOutputStream(destination);
                try {
                    FileChannel sourceChannel = sourceStream.getChannel();
                    sourceChannel.transferTo(0, sourceChannel.size(), destinationStream.getChannel());
                } finally {
                    try {
                        destinationStream.close();
                    } catch (Exception e) {
                        throw new RuntimeIoException(e, IoMode.CLOSE);
                    }
                }
            } finally {
                try {
                    sourceStream.close();
                } catch (Exception e) {
                    throw new RuntimeIoException(e, IoMode.CLOSE);
                }
            }
        } catch (IOException e) {
            throw new RuntimeIoException(e, IoMode.COPY);
        }
    }
