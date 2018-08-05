    public static double[][] correlation(double matrix[][]) {
        double[][] covMatrix = covariance(matrix);
        int m = covMatrix.length;
        double[] vectorDiagonal = new double[m];
        for (int i = 0; i < m; i++) {
            if (covMatrix[i].length != m) throw new IllegalArgumentException();
            vectorDiagonal[i] = Math.sqrt(covMatrix[i][i]);
        }
        double[][] corMatrix = covMatrix;
        for (int v1 = 0; v1 < m; v1++) {
            for (int v2 = v1; v2 < m; v2++) {
                corMatrix[v1][v2] = covMatrix[v1][v2] / (vectorDiagonal[v1] * vectorDiagonal[v2]);
                corMatrix[v2][v1] = corMatrix[v1][v2];
            }
        }
        return corMatrix;
    }
