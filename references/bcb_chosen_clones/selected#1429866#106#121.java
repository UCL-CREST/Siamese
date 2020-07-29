    static double[][] symm(double[][] dMatrix) {
        for (int i = 0; i < dMatrix.length; i++) {
            for (int j = i; j < dMatrix.length; j++) {
                if (i == j) {
                    dMatrix[i][j] = 0;
                    dMatrix[j][i] = 0;
                }
                {
                    double dist = (dMatrix[i][j] + dMatrix[j][i]) / 2.0;
                    dMatrix[i][j] = dist;
                    dMatrix[j][i] = dMatrix[i][j];
                }
            }
        }
        return dMatrix;
    }
