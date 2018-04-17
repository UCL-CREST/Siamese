    private static TupleSpace loadTuplesFromFile(String fileName) throws IOException, TupleSpaceException {
        StringTokenizer stringTokenizer;
        TupleSpace tupleSpace = new TupleSpace();
        Tuple tuple;
        File input = new File(fileName);
        BufferedReader buffer = new BufferedReader(new FileReader(input));
        String string = buffer.readLine();
        String token;
        Float floatElem;
        while (string != null) {
            stringTokenizer = new StringTokenizer(string, ",");
            tuple = new Tuple();
            while (stringTokenizer.hasMoreTokens()) {
                token = stringTokenizer.nextToken();
                try {
                    floatElem = new Float(token);
                    tuple.addActual(floatElem);
                } catch (Exception e) {
                    tuple.addActual(token);
                }
            }
            tupleSpace.out(tuple);
            string = buffer.readLine();
        }
        return tupleSpace;
    }
