    public static FredOutVal readHillClimbOutput(File hClimbOut) throws IOException {
        BufferedReader input = new BufferedReader(new FileReader(hClimbOut));
        String nextLine = input.readLine();
        StringTokenizer tok;
        while (nextLine != null) {
            tok = new StringTokenizer(nextLine);
            if (tok.hasMoreTokens()) {
                if (tok.nextToken().equals("Minimum")) {
                    break;
                }
            }
            nextLine = input.readLine();
        }
        if (nextLine == null) {
            System.out.println("Bad hillclimb output file");
            return null;
        }
        nextLine = input.readLine();
        nextLine = input.readLine();
        tok = new StringTokenizer(nextLine);
        double omega = (new Double(tok.nextToken())).doubleValue();
        double sigma = (new Double(tok.nextToken())).doubleValue();
        int npop = (int) ((new Double(tok.nextToken())).doubleValue() + 0.5);
        omega = Math.exp(omega);
        sigma = Math.exp(sigma);
        double[] percentages = new double[6];
        FredOutVal returnVal = new FredOutVal(omega, sigma, npop, 1.0e25, percentages);
        input.close();
        return returnVal;
    }
