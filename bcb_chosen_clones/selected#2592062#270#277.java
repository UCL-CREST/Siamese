    public Matrix transpose() {
        final int array[][] = new int[numCols][numRows];
        for (int j, i = 0; i < numRows; i++) {
            array[0][i] = matrix[i][0];
            for (j = 1; j < numCols; j++) array[j][i] = matrix[i][j];
        }
        return new IntegerSquareMatrix(array);
    }
