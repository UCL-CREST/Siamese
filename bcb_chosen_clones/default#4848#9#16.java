    static void BuildSqSymmBasisMatrix(double[][] lSVsqss, int lnv, double[][] lelectrodesub, int lnChan) {
        for (int j = 0; j < lnv; j++) {
            for (int k = 0; k <= j; k++) {
                lSVsqss[j][k] = Dot(lelectrodesub[j], lelectrodesub[k], lnChan);
                if (k != j) lSVsqss[k][j] = lSVsqss[j][k];
            }
        }
    }
