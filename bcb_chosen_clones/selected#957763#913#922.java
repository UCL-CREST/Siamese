    public static double[][] transpose(final double[][] A) {
        int N = A.length;
        double[][] B = alloc(N, N);
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                B[r][c] = A[c][r];
            }
        }
        return B;
    }
