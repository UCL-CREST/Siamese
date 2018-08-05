    public static double[][] transp(double[][] m1) {
        int m = m1.length;
        int n = m1[0].length;
        double[][] ret = new double[n][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                ret[j][i] = m1[i][j];
            }
        }
        return ret;
    }
