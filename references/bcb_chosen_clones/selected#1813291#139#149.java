    public static void copyFile(File source, String target) throws FileNotFoundException, IOException {
        File fout = new File(target);
        fout.mkdirs();
        fout.delete();
        fout = new File(target);
        FileChannel in = new FileInputStream(source).getChannel();
        FileChannel out = new FileOutputStream(target).getChannel();
        in.transferTo(0, in.size(), out);
        in.close();
        out.close();
    }
