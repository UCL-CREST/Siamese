    public static void copyFile(File src, File dest) throws IOException {
        log.debug("Copying file: '" + src + "' to '" + dest + "'");
        FileChannel srcChannel = new FileInputStream(src).getChannel();
        FileChannel dstChannel = new FileOutputStream(dest).getChannel();
        dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
        srcChannel.close();
        dstChannel.close();
    }
