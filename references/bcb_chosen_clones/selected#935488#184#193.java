    public static double[][] transpose(double[][] mat) {
        int m = mat.length;
        int n = mat[0].length;
        double[][] res = new double[n][];
        for (int i = 0; i < n; ++i) {
            res[i] = new double[m];
            for (int j = 0; j < m; ++j) res[i][j] = mat[j][i];
        }
        return (res);
    }
