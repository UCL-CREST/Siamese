    public Matrix transpose() {
        Matrix matrixT = new Matrix(columnCount, rowCount);
        int i, j;
        for (i = 0; i < rowCount; i++) {
            for (j = 0; j < columnCount; j++) {
                matrixT.matrix[j][i] = matrix[i][j];
            }
        }
        return matrixT;
    }
