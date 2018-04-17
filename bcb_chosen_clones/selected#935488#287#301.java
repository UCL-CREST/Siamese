    public static double[][] square(double[][] mat) {
        int m = Matrix.getNumOfRows(mat);
        int n = Matrix.getNumOfColumns(mat);
        double[][] res = Matrix.newMatrix(m, m);
        for (int i = 0; i < m; ++i) {
            res[i][i] = 0.0;
            for (int k = 0; k < n; ++k) res[i][i] += mat[i][k] * mat[i][k];
            for (int j = 0; j < i; ++j) {
                res[i][j] = 0.0;
                for (int k = 0; k < n; ++k) res[i][j] += mat[i][k] * mat[j][k];
                res[j][i] = res[i][j];
            }
        }
        return (res);
    }
