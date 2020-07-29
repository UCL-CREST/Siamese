    void eigendecomposition(int flgforce) {
        int i, j;
        if (countCupdatesSinceEigenupdate == 0 && flgforce < 2) return;
        if (flgforce > 0 || (timings.eigendecomposition < 1000 + options.maxTimeFractionForEigendecomposition * (System.currentTimeMillis() - timings.start) && countCupdatesSinceEigenupdate > 1. / sp.getCcov() / N / 5.)) {
            for (i = 0; i < N; ++i) for (j = 0; j <= i; ++j) B[i][j] = B[j][i] = C[i][j];
            double[] offdiag = new double[N];
            long firsttime = System.currentTimeMillis();
            tred2(N, B, diagD, offdiag);
            tql2(N, diagD, offdiag, B);
            timings.eigendecomposition += System.currentTimeMillis() - firsttime;
            if (options.checkEigenSystem > 0) checkEigenSystem(N, C, diagD, B);
            for (i = 0; i < N; ++i) {
                if (diagD[i] < 0) error("an eigenvalue has become negative");
                diagD[i] = Math.sqrt(diagD[i]);
            }
            if (math.min(diagD) == 0) axisratio = Double.POSITIVE_INFINITY; else axisratio = math.max(diagD) / math.min(diagD);
            countCupdatesSinceEigenupdate = 0;
        }
    }
