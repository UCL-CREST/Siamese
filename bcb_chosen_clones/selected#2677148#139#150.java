    public Matrix getTransposed() {
        if (Transposed != null) return Transposed;
        Matrix result = new Matrix(Cols, Rows);
        for (int i = 0; i < Rows; ++i) {
            for (int j = 0; j < Cols; ++j) {
                result.Values[j][i] = Values[i][j];
            }
        }
        Transposed = result;
        result.Transposed = this;
        return result;
    }
