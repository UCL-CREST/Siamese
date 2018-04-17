    static void copyFile(File in, File out) throws IOException {
        FileChannel source = new FileInputStream(in).getChannel();
        FileChannel destination = new FileOutputStream(out).getChannel();
        source.transferTo(0, source.size(), destination);
        source.close();
        destination.close();
    }
