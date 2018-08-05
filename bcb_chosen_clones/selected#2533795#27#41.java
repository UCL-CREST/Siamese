    public double[][] distanceMatrix(final AISNonDominatedSolutionList visSolutionSet) {
        VISSolution solutionI, solutionJ;
        final double[][] limits_ = visSolutionSet.getObjectiveBounds();
        double[][] distance = new double[visSolutionSet.size()][visSolutionSet.size()];
        for (int i = 0; i < visSolutionSet.size(); i++) {
            distance[i][i] = 0.0;
            solutionI = (VISSolution) visSolutionSet.get(i);
            for (int j = i + 1; j < visSolutionSet.size(); j++) {
                solutionJ = (VISSolution) visSolutionSet.get(j);
                distance[i][j] = distanceBetweenObjectives(solutionI, solutionJ, limits_);
                distance[j][i] = distance[i][j];
            }
        }
        return distance;
    }
