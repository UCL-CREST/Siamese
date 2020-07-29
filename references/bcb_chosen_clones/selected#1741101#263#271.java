    public static void copyFile(File src, File dest) throws IOException {
        FileInputStream fis = new FileInputStream(src);
        FileOutputStream fos = new FileOutputStream(dest);
        FileChannel channelSrc = fis.getChannel();
        FileChannel channelDest = fos.getChannel();
        channelSrc.transferTo(0, channelSrc.size(), channelDest);
        fis.close();
        fos.close();
    }
