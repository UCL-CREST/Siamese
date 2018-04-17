    public void rescale() {
        double[][] sim = new double[numSeqs][numSeqs];
        for (int i = 0; i < numSeqs; i++) {
            for (int j = i; j < numSeqs; j++) {
                if (this.getDistance(i, j) > 0) {
                    sim[i][j] = (this.getDistance(i, j) - kMin) * 2 / (kMax - kMin);
                }
                sim[j][i] = sim[i][j];
            }
        }
        setDistances(sim);
        System.out.println("K rescaled");
    }
