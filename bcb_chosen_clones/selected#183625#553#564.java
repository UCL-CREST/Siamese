    public static double[][] transposeMatrix(double[][] matrix) {
        int nRows = matrix.length;
        int nColumns = matrix[0].length;
        for (int i = 1; i < nRows; i++) if (matrix[i].length != nColumns) throw new IllegalArgumentException("All rows must be the same length");
        double[][] transpose = new double[nColumns][nRows];
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nColumns; j++) {
                transpose[j][i] = matrix[i][j];
            }
        }
        return transpose;
    }
