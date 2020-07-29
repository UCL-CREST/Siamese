    public static ArrayList<FredOutVal> readFredOutVals(File filename) throws IOException {
        BufferedReader input;
        try {
            input = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist");
            return null;
        }
        StringTokenizer reader;
        ArrayList<FredOutVal> returnVals = new ArrayList<FredOutVal>();
        double[] vals = new double[4];
        double[] percentages = new double[6];
        Double convertedVal;
        String nextLine = input.readLine();
        String token;
        while (nextLine != null) {
            reader = new StringTokenizer(nextLine);
            for (int i = 0; i < vals.length; i++) {
                if (!reader.hasMoreTokens()) {
                    throw new IndexOutOfBoundsException("Invalid input file for Fred sorting");
                }
                token = reader.nextToken();
                token = token.substring(0, token.length() - 1);
                convertedVal = new Double(token);
                vals[i] = convertedVal.doubleValue();
            }
            for (int i = 0; i < percentages.length; i++) {
                if (!reader.hasMoreTokens()) {
                    throw new IndexOutOfBoundsException("Invalid input file for Fred sorting");
                }
                token = reader.nextToken();
                if (i != percentages.length - 1) {
                    token = token.substring(0, token.length() - 1);
                }
                convertedVal = new Double(token);
                percentages[i] = convertedVal.doubleValue();
            }
            FredOutVal nextVal = new FredOutVal(vals[0], vals[1], (int) vals[2], vals[3], percentages.clone());
            returnVals.add(nextVal);
            nextLine = input.readLine();
        }
        input.close();
        return returnVals;
    }
