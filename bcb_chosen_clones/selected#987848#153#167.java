    public void cutOff() {
        double[][] sim = new double[numSeqs][numSeqs];
        for (int i = 0; i < numSeqs; i++) {
            for (int j = i + 0; j < numSeqs; j++) {
                if (this.getDistance(i, j) > kAvg) {
                    sim[i][j] = this.getDistance(i, j);
                } else {
                    sim[i][j] = kAvg;
                }
                sim[j][i] = sim[i][j];
            }
        }
        kMin = kAvg;
        setDistances(sim);
    }
