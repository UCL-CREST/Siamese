    public static final float[][] transpose4x4Matrix(float[][] m) {
        float[][] matrix = new float[4][4];
        for (int i = 0; i < 4; ++i) for (int j = 0; j < 4; ++j) matrix[i][j] = m[j][i];
        return matrix;
    }
