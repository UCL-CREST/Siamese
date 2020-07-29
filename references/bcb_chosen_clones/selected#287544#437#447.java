    private static void copyFile(String src, String dest) throws IOException {
        File destFile = new File(dest);
        if (destFile.exists()) {
            destFile.delete();
        }
        FileChannel srcChannel = new FileInputStream(src).getChannel();
        FileChannel dstChannel = new FileOutputStream(dest).getChannel();
        dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
        srcChannel.close();
        dstChannel.close();
    }
