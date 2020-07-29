    public static File copy(String inFileName, String outFileName) throws IOException {
        File inputFile = new File(inFileName);
        File outputFile = new File(outFileName);
        FileReader in = new FileReader(inputFile);
        FileWriter out = new FileWriter(outputFile);
        int c;
        while ((c = in.read()) != -1) out.write(c);
        in.close();
        out.close();
        return outputFile;
    }
