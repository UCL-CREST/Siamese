    public double[][] getHessian(MultiVarFunction Veq, double[] x, double[] D, double[] dx) {
        int m = x.length;
        if (xp == null || xp.length != m) {
            allocateArrays(m);
        }
        for (int i = 0; i < m; i++) {
            for (int j = i; j < m; j++) {
                if (i == j) {
                    for (int k = 0; k < m; k++) {
                        xp[k] = x[k];
                        xm[k] = x[k];
                    }
                    xp[i] = x[i] + dx[i];
                    xm[i] = x[i] - dx[i];
                    H[i][i] = (Veq.evaluate(xp) - 2.0 * Veq.evaluate(x) + Veq.evaluate(xm)) / (dx[i] * dx[i]);
                } else {
                    for (int k = 0; k < m; k++) {
                        xpp[k] = x[k];
                        xpm[k] = x[k];
                        xmp[k] = x[k];
                        xmm[k] = x[k];
                    }
                    xpp[i] = x[i] + dx[i];
                    xpp[j] = x[j] + dx[j];
                    xpm[i] = x[i] + dx[i];
                    xpm[j] = x[j] - dx[j];
                    xmp[i] = x[i] - dx[i];
                    xmp[j] = x[j] + dx[j];
                    xmm[i] = x[i] - dx[i];
                    xmm[j] = x[j] - dx[j];
                    H[i][j] = ((Veq.evaluate(xpp) - Veq.evaluate(xpm)) / (2.0 * dx[j]) - (Veq.evaluate(xmp) - Veq.evaluate(xmm)) / (2.0 * dx[j])) / (2.0 * dx[i]);
                    H[j][i] = H[i][j];
                }
            }
        }
        for (int i = 0; i < m; i++) {
            for (int k = 0; k < m; k++) {
                xp[k] = x[k];
                xm[k] = x[k];
            }
            xp[i] = x[i] + dx[i];
            xm[i] = x[i] - dx[i];
            D[i] = -(Veq.evaluate(xp) - Veq.evaluate(xm)) / (2.0 * dx[i]);
        }
        return H;
    }
