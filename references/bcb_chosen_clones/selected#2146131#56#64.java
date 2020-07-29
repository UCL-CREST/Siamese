    public static void copyFile(File inputFile, File outputFile) throws IOException {
        BufferedInputStream fr = new BufferedInputStream(new FileInputStream(inputFile));
        BufferedOutputStream fw = new BufferedOutputStream(new FileOutputStream(outputFile));
        byte[] buf = new byte[8192];
        int n;
        while ((n = fr.read(buf)) >= 0) fw.write(buf, 0, n);
        fr.close();
        fw.close();
    }
