    public void copyFile(FileInputStream fis, File out) throws IOException {
        FileChannel inChannel = fis.getChannel();
        FileChannel outChannel = new FileOutputStream(out).getChannel();
        copyChannel(inChannel, outChannel);
    }
