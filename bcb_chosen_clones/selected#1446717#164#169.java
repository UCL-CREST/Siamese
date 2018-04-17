    public Matrix transpose() {
        Matrix result = new Matrix(columns, rows);
        int i, j;
        for (i = 0; i < rows; i++) for (j = 0; j < columns; j++) result.matrix[j][i] = matrix[i][j];
        return result;
    }
