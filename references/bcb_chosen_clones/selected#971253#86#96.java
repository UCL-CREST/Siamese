    public static void copyFile(File srcFile, File destFile) throws IOException {
        logger.debug("copyFile(srcFile={}, destFile={}) - start", srcFile, destFile);
        FileChannel srcChannel = new FileInputStream(srcFile).getChannel();
        FileChannel dstChannel = new FileOutputStream(destFile).getChannel();
        try {
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
        } finally {
            srcChannel.close();
            dstChannel.close();
        }
    }
