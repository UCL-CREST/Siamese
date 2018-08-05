    public void calculateDistances() {
        distances = new double[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = i + 1; j < dimension; j++) {
                distances[i][j] = calculatePairwiseDistance(i, j);
                distances[j][i] = distances[i][j];
            }
            distances[i][i] = 0.0;
        }
        distancesKnown = true;
    }
