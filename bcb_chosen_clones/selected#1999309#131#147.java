    public void copy(String pathFileIn, String pathFileOut) {
        try {
            File inputFile = new File(pathFileIn);
            File outputFile = new File(pathFileOut);
            FileReader in = new FileReader(inputFile);
            File outDir = new File(DirOut);
            if (!outDir.exists()) outDir.mkdirs();
            FileWriter out = new FileWriter(outputFile);
            int c;
            while ((c = in.read()) != -1) out.write(c);
            in.close();
            out.close();
            this.printColumn(inputFile.getName(), outputFile.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
