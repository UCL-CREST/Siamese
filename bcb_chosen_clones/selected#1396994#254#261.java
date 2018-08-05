    public void transpose(IMatrix result) {
        if ((result.rows != rows) || (result.columns != columns)) result.reshape(rows, columns);
        int i, j;
        for (i = 0; i < rows; i++) for (j = 0; j < columns; j++) {
            result.realmatrix[j][i] = realmatrix[i][j];
            result.imagmatrix[j][i] = imagmatrix[i][j];
        }
    }
