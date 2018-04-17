    public Matrix transpose() {
        int i, j;
        Matrix mResult = new Matrix(this.nCols, this.nLines);
        for (i = 0; i < mResult.nLines; i++) {
            for (j = 0; j < mResult.nCols; j++) {
                mResult.cell[i][j] = cell[j][i];
            }
        }
        return mResult;
    }
