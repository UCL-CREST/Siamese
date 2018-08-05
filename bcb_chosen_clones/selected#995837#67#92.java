    public JavaCodeAnalyzer(String filenameIn, String filenameOut, String lineLength) {
        try {
            File tmp = File.createTempFile("JavaCodeAnalyzer", "tmp");
            BufferedReader br = new BufferedReader(new FileReader(filenameIn));
            BufferedWriter out = new BufferedWriter(new FileWriter(tmp));
            while (br.ready()) {
                out.write(br.read());
            }
            br.close();
            out.close();
            jco = new JavaCodeOutput(tmp, filenameOut, lineLength);
            SourceCodeParser p = new JavaCCParserFactory().createParser(new FileReader(tmp), null);
            List statements = p.parseCompilationUnit();
            ListIterator it = statements.listIterator();
            eh = new ExpressionHelper(this, jco);
            Node n;
            printLog("Parsed file " + filenameIn + "\n");
            while (it.hasNext()) {
                n = (Node) it.next();
                parseObject(n);
            }
            tmp.delete();
        } catch (Exception e) {
            System.err.println(getClass() + ": " + e);
        }
    }
