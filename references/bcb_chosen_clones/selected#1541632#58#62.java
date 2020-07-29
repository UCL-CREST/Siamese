    public Matrix transpose() {
        float[][] res = new float[dim(1)][dim(0)];
        for (int i = 0; i < values.length; ++i) for (int j = 0; j < values[i].length; ++j) res[j][i] = values[i][j];
        return new Matrix(res);
    }
