    public static void copyFile(File from, File to) throws IOException {
        ensureFile(to);
        FileChannel srcChannel = new FileInputStream(from).getChannel();
        FileChannel dstChannel = new FileOutputStream(to).getChannel();
        dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
        srcChannel.close();
        dstChannel.close();
    }
