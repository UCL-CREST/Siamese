    void logCorrectMatrix(double p) {
        for (int i = 0; i < numdata; i++) {
            for (int j = 0; j < i; j++) {
                depMatrix[i][j] = 1 / Math.pow(depMatrix[i][j], p);
                depMatrix[j][i] = depMatrix[i][j];
            }
        }
    }
