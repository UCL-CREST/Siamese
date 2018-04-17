    public static void copyFile(File dst, File src, boolean append) throws FileNotFoundException, IOException {
        dst.createNewFile();
        FileChannel in = new FileInputStream(src).getChannel();
        FileChannel out = new FileOutputStream(dst).getChannel();
        long startAt = 0;
        if (append) startAt = out.size();
        in.transferTo(startAt, in.size(), out);
        out.close();
        in.close();
    }
