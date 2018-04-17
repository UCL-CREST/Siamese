    public static final float[][] transpose3x3Matrix(float[][] m) {
        float[][] matrix = new float[4][4];
        for (int i = 0; i < 3; ++i) for (int j = 0; j < 3; ++j) matrix[i][j] = m[j][i];
        return matrix;
    }
