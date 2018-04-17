    public static void copyFile(String input, String output) {
        try {
            File inputFile = new File(input);
            File outputFile = new File(output);
            FileReader in;
            in = new FileReader(inputFile);
            FileWriter out = new FileWriter(outputFile);
            int c;
            while ((c = in.read()) != -1) out.write(c);
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
