    private static FileChannel getFileChannel(File file, boolean isOut, boolean append) throws OpenR66ProtocolSystemException {
        FileChannel fileChannel = null;
        try {
            if (isOut) {
                FileOutputStream fileOutputStream = new FileOutputStream(file.getPath(), append);
                fileChannel = fileOutputStream.getChannel();
                if (append) {
                    try {
                        fileChannel.position(file.length());
                    } catch (IOException e) {
                    }
                }
            } else {
                if (!file.exists()) {
                    throw new OpenR66ProtocolSystemException("File does not exist");
                }
                FileInputStream fileInputStream = new FileInputStream(file.getPath());
                fileChannel = fileInputStream.getChannel();
            }
        } catch (FileNotFoundException e) {
            throw new OpenR66ProtocolSystemException("File not found", e);
        }
        return fileChannel;
    }
