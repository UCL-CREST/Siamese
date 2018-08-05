    public ContourGenerator(URL url, float modelMean, float modelStddev) throws IOException {
        this.modelMean = modelMean;
        this.modelStddev = modelStddev;
        List termsList = new ArrayList();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        line = reader.readLine();
        while (line != null) {
            if (!line.startsWith("***")) {
                parseAndAdd(termsList, line);
            }
            line = reader.readLine();
        }
        terms = (F0ModelTerm[]) termsList.toArray(terms);
        reader.close();
    }
