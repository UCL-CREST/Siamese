    public static void copyfile(String src, String dst) throws IOException {
        dst = new File(dst).getAbsolutePath();
        new File(new File(dst).getParent()).mkdirs();
        FileChannel srcChannel = new FileInputStream(src).getChannel();
        FileChannel dstChannel = new FileOutputStream(dst).getChannel();
        dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
        srcChannel.close();
        dstChannel.close();
    }
