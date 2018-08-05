    void copyFileAscii(String src, String dest) {
        try {
            File inputFile = new File(src);
            File outputFile = new File(dest);
            FileReader in = new FileReader(inputFile);
            FileWriter out = new FileWriter(outputFile);
            int c;
            while ((c = in.read()) != -1) out.write(c);
            in.close();
            out.close();
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
    }
