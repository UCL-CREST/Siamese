    private static void copyFile(File src, File dst) throws IOException {
        FileChannel in = new FileInputStream(src).getChannel();
        FileChannel out = new FileOutputStream(dst).getChannel();
        in.transferTo(0, in.size(), out);
        in.close();
        out.close();
    }
