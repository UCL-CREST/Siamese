    static void copyFile(File file, File file1) throws IOException {
        byte abyte0[] = new byte[512];
        FileInputStream fileinputstream = new FileInputStream(file);
        FileOutputStream fileoutputstream = new FileOutputStream(file1);
        int i;
        while ((i = fileinputstream.read(abyte0)) > 0) fileoutputstream.write(abyte0, 0, i);
        fileinputstream.close();
        fileoutputstream.close();
    }
