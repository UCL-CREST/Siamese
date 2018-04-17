    public static void copy(FileInputStream in, FileOutputStream out) throws IOException {
        FileChannel fcIn = in.getChannel();
        FileChannel fcOut = out.getChannel();
        fcIn.transferTo(0, fcIn.size(), fcOut);
    }
