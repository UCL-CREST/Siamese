    public Matrix transpose() {
        Matrix matrix = new Matrix();
        matrix.m = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                matrix.m[j][i] = m[i][j];
            }
        }
        return matrix;
    }
