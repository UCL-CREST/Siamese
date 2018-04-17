    public static void copy(String fromFile, String toFile) throws IOException {
        File inputFile = new File(fromFile);
        File outputFile = new File(toFile);
        FileReader in = new FileReader(inputFile);
        FileWriter out = new FileWriter(outputFile);
        int c;
        while ((c = in.read()) != -1) out.write(c);
        in.close();
        out.close();
    }
