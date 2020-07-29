    public void toSimilarity() {
        double[][] sim = new double[numSeqs][numSeqs];
        for (int i = 0; i < numSeqs; i++) {
            for (int j = i; j < numSeqs; j++) {
                sim[i][j] = 1 - this.getDistance(i, j);
                sim[j][i] = sim[i][j];
            }
        }
        setDistances(sim);
    }
