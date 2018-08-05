    private static String makeTempTraceFile(String base) throws IOException {
        File temp = File.createTempFile(base, ".trace");
        temp.deleteOnExit();
        FileChannel dstChannel = new FileOutputStream(temp).getChannel();
        FileChannel srcChannel = new FileInputStream(base + ".key").getChannel();
        long size = dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
        srcChannel.close();
        srcChannel = new FileInputStream(base + ".data").getChannel();
        dstChannel.transferFrom(srcChannel, size, srcChannel.size());
        srcChannel.close();
        dstChannel.close();
        return temp.getPath();
    }
