    public void copyTo(String newname) throws IOException {
        FileChannel srcChannel = new FileInputStream(dosname).getChannel();
        FileChannel dstChannel = new FileOutputStream(newname).getChannel();
        dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
        srcChannel.close();
        dstChannel.close();
    }
