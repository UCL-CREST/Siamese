    protected static double[][] buildDistancesMatrix(PairwiseDistanceCalculator pairwiseDistanceCalculator, int dimension, boolean useTwiceMaximumDistanceWhenPairwiseDistanceNotCalculatable, ProgressListener progress) throws CannotBuildDistanceMatrixException {
        double[][] distances = new double[dimension][dimension];
        float tot = (dimension * (dimension - 1)) / 2;
        int done = 0;
        final double noDistance = -1;
        double maxDistance = -1;
        for (int i = 0; i < dimension; ++i) {
            for (int j = i + 1; j < dimension; ++j) {
                try {
                    distances[i][j] = pairwiseDistanceCalculator.calculatePairwiseDistance(i, j);
                    maxDistance = Math.max(distances[i][j], maxDistance);
                } catch (CannotBuildDistanceMatrixException e) {
                    if (!useTwiceMaximumDistanceWhenPairwiseDistanceNotCalculatable) {
                        throw e;
                    }
                    distances[i][j] = noDistance;
                }
                distances[j][i] = distances[i][j];
                if (progress != null) progress.setProgress(++done / tot);
            }
        }
        if (maxDistance < 0) {
            throw new CannotBuildDistanceMatrixException("It is not possible to compute the Tamura-Nei genetic distance " + "for these sequences because no pair of sequences overlap in the alignment.");
        }
        for (int i = 0; i < dimension; ++i) {
            for (int j = i + 1; j < dimension; ++j) {
                if (distances[i][j] == noDistance) {
                    distances[i][j] = distances[j][i] = maxDistance * 2;
                }
            }
        }
        return distances;
    }
