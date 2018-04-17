    public static double[][] dissim(int nrow, int ncol, double[] mass, double[][] A) {
        double[][] Adiss = new double[nrow][nrow];
        for (int i1 = 0; i1 < nrow; i1++) {
            for (int i2 = 0; i2 < nrow; i2++) {
                Adiss[i1][i2] = 0.0;
            }
        }
        for (int i1 = 0; i1 < nrow; i1++) {
            for (int i2 = 0; i2 < i1; i2++) {
                for (int j = 0; j < ncol; j++) {
                    Adiss[i1][i2] += 0.5 * Math.pow(A[i1][j] - A[i2][j], 2.0);
                }
                Adiss[i2][i1] = Adiss[i1][i2];
            }
        }
        return Adiss;
    }
