    public static void copyFile(File src, File dst) throws IOException {
        File inputFile = src;
        File outputFile = dst;
        FileReader in = new FileReader(inputFile);
        FileWriter out = new FileWriter(outputFile);
        int c;
        while ((c = in.read()) != -1) out.write(c);
        in.close();
        out.close();
    }
