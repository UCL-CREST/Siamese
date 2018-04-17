    private void updateNumStocks() {
        int oldNumStocks = expectedReturns.length;
        double[] oldExpRets = expectedReturns;
        double[] oldVariances = variances;
        double[][] oldCovrs = covariances;
        expectedReturns = new double[numStocks];
        variances = new double[numStocks];
        covariances = new double[numStocks][numStocks];
        int limitingFactor = (numStocks > oldNumStocks) ? oldNumStocks : numStocks;
        for (int i = 0; i < limitingFactor; i++) {
            expectedReturns[i] = oldExpRets[i];
            variances[i] = oldVariances[i];
            covariances[i][i] = oldCovrs[i][i];
            for (int j = 0; j < i; j++) {
                covariances[i][j] = covariances[j][i] = oldCovrs[i][j];
            }
        }
    }
