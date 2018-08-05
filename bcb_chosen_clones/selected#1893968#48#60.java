    public static double[][] triangularToComplete(double[][] omatrix) {
        int n = omatrix.length + 1;
        double[][] M = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (i < j) {
                    M[i][j] = omatrix[i][j - 1 - i];
                    M[j][i] = M[i][j];
                }
            }
        }
        return M;
    }
