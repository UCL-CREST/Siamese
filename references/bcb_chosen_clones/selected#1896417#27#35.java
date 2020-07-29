    public Matrix transpose() {
        double[][] out = new double[cols][rows];
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                out[i][j] = value[j][i];
            }
        }
        return new Matrix(out, cols, rows);
    }
