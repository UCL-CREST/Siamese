    public static void copyFile(File srcFile, File dstFile) {
        logger.info("Create file : " + dstFile.getPath());
        try {
            FileChannel srcChannel = new FileInputStream(srcFile).getChannel();
            FileChannel dstChannel = new FileOutputStream(dstFile).getChannel();
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
            srcChannel.close();
            dstChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
