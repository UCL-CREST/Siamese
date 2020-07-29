    private void convertScoresToDistances() {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < adjacencyWeightMatrix.length; i++) {
            for (int j = 0; j < adjacencyWeightMatrix[i].length; j++) {
                if (maximumWeight < adjacencyWeightMatrix[i][j]) {
                    maximumWeight = adjacencyWeightMatrix[i][j];
                }
                if (min > adjacencyWeightMatrix[i][j]) {
                    min = adjacencyWeightMatrix[i][j];
                }
            }
        }
        if (min < 0) {
            System.err.println("error: smallest value in adjacency weight matrix was negative");
        }
        for (int i = 0; i < adjacencyWeightMatrix.length; i++) {
            for (int j = i; j < adjacencyWeightMatrix[i].length; j++) {
                adjacencyWeightMatrix[i][j] = maximumWeight - adjacencyWeightMatrix[i][j];
                adjacencyWeightMatrix[j][i] = adjacencyWeightMatrix[i][j];
            }
        }
        for (int i = 0; i < numberOfContigs; i++) {
            adjacencyWeightMatrix[i][i + numberOfContigs] = 0;
            adjacencyWeightMatrix[i + numberOfContigs][i] = 0;
        }
    }
