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
                    tuple.add(new Field().setValue(floatElem));
                } catch (Exception e) {
                    tuple.add(new Field().setValue(token));
                }
            }
            tupleSpace.out(tuple);
            string = buffer.readLine();
        }
        return tupleSpace;
    }
