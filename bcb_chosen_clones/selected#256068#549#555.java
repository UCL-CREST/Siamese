    public static void copyFile(File source, File target) throws IOException {
        FileChannel in = (new FileInputStream(source)).getChannel();
        FileChannel out = (new FileOutputStream(target)).getChannel();
        in.transferTo(0, source.length(), out);
        in.close();
        out.close();
    }
