    private void stripOneFilex(File inFile, File outFile) throws IOException {
        StreamTokenizer reader = new StreamTokenizer(new FileReader(inFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
        reader.slashSlashComments(false);
        reader.slashStarComments(false);
        reader.eolIsSignificant(true);
        int token;
        while ((token = reader.nextToken()) != StreamTokenizer.TT_EOF) {
            switch(token) {
                case StreamTokenizer.TT_NUMBER:
                    throw new IllegalStateException("didn't expect TT_NUMBER: " + reader.nval);
                case StreamTokenizer.TT_WORD:
                    System.out.print(reader.sval);
                    writer.write("WORD:" + reader.sval, 0, reader.sval.length());
                default:
                    char outChar = (char) reader.ttype;
                    System.out.print(outChar);
                    writer.write(outChar);
            }
        }
    }
