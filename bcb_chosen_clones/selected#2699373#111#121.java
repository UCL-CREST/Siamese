    private String copyTutorial() throws IOException {
        File inputFile = new File(getFilenameForOriginalTutorial());
        File outputFile = new File(getFilenameForCopiedTutorial());
        FileReader in = new FileReader(inputFile);
        FileWriter out = new FileWriter(outputFile);
        int c;
        while ((c = in.read()) != -1) out.write(c);
        in.close();
        out.close();
        return getFilenameForCopiedTutorial();
    }
