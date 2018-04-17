    void eigendecomposition(int flgforce) {
        int i, j;
        if (countCupdatesSinceEigenupdate == 0 && flgforce < 2) return;
        if (flgdiag) {
            for (i = 0; i < N; ++i) {
                diagD[i] = Math.sqrt(C[i][i]);
            }
            countCupdatesSinceEigenupdate = 0;
        } else {
            for (i = 0; i < N; ++i) for (j = 0; j <= i; ++j) B[i][j] = B[j][i] = C[i][j];
            double[] offdiag = new double[N];
            tred2(N, B, diagD, offdiag);
            tql2(N, diagD, offdiag, B);
            for (i = 0; i < N; ++i) {
                if (diagD[i] < 0) System.out.println("an eigenvalue has become negative");
                diagD[i] = Math.sqrt(diagD[i]);
            }
            countCupdatesSinceEigenupdate = 0;
        }
        if (math.min(diagD) == 0) axisratio = Double.POSITIVE_INFINITY; else axisratio = math.max(diagD) / math.min(diagD);
    }
