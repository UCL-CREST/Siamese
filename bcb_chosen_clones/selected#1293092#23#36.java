    public double[][] distanceMatrix(SolutionSet solutionSet) {
        Solution solutionI, solutionJ;
        double[][] distance = new double[solutionSet.size()][solutionSet.size()];
        for (int i = 0; i < solutionSet.size(); i++) {
            distance[i][i] = 0.0;
            solutionI = solutionSet.get(i);
            for (int j = i + 1; j < solutionSet.size(); j++) {
                solutionJ = solutionSet.get(j);
                distance[i][j] = this.distanceBetweenObjectives(solutionI, solutionJ);
                distance[j][i] = distance[i][j];
            }
        }
        return distance;
    }
