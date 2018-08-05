    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        FileInputStream iStream = new FileInputStream(sourceFile);
        FileOutputStream oStream = new FileOutputStream(targetFile);
        FileChannel inChannel = iStream.getChannel();
        FileChannel outChannel = oStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (true) {
            buffer.clear();
            int readCount = inChannel.read(buffer);
            if (readCount == -1) {
                break;
            }
            buffer.flip();
            outChannel.write(buffer);
        }
        iStream.close();
        oStream.close();
    }
