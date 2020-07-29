    public Matrix transpose() {
        final double array[][] = new double[numCols][numRows];
        for (int j, i = 0; i < numRows; i++) {
            array[0][i] = matrix[i][0];
            for (j = 1; j < numCols; j++) array[j][i] = matrix[i][j];
        }
        return new DoubleMatrix(array);
    }
