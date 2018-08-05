    public static void copyFile(File oldFile, File newFile) throws Exception {
        newFile.getParentFile().mkdirs();
        newFile.createNewFile();
        FileChannel srcChannel = new FileInputStream(oldFile).getChannel();
        FileChannel dstChannel = new FileOutputStream(newFile).getChannel();
        dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
        srcChannel.close();
        dstChannel.close();
    }
