    public static double[] leastSquares(Function s, double x[], double y[]) throws SingularMatrixException {
        int n = x.length;
        int m = s.f(0.).length;
        double A[][] = new double[n][];
        for (int i = 0; i < n; i++) A[i] = s.f(x[i]);
        double S[][] = new double[m][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j <= i; j++) {
                S[i][j] = 0.;
                for (int k = 0; k < n; k++) S[i][j] += A[k][i] * A[k][j];
                S[j][i] = S[i][j];
            }
        }
        double T[] = new double[m];
        for (int i = 0; i < m; i++) {
            T[i] = 0.;
            for (int j = 0; j < n; j++) T[i] += A[j][i] * y[j];
        }
        return solveNxN(S, T);
    }
