    public Matrix transpose() {
        final Ring.Member array[][] = new Ring.Member[numCols][numRows];
        for (int j, i = 0; i < numRows; i++) {
            array[0][i] = matrix[i][0];
            for (j = 1; j < numCols; j++) array[j][i] = matrix[i][j];
        }
        return new RingMatrix(array);
    }
