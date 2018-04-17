    public static void copy(String file1, String file2) throws IOException {
        File inputFile = new File(file1);
        File outputFile = new File(file2);
        FileReader in = new FileReader(inputFile);
        FileWriter out = new FileWriter(outputFile);
        System.out.println("Copy file from: " + file1 + " to: " + file2);
        int c;
        while ((c = in.read()) != -1) out.write(c);
        in.close();
        out.close();
    }
