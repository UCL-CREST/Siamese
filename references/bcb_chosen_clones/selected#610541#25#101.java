    public MultiDimensionalScaling(float[][] data, int anpcs) {
        n = data.length;
        npcs = anpcs;
        if (npcs > n) {
            npcs = n;
        }
        out = new float[n][npcs];
        double[][] d = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                d[i][j] = data[i][j];
                d[j][i] = data[i][j];
            }
        }
        double max = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                for (int k = 0; k < j; k++) {
                    max = Math.max(max, d[j][k] - d[i][j] - d[i][k]);
                }
            }
        }
        if (max > 0) {
            max *= 10;
        } else {
            max = 0;
        }
        System.out.println("Triangle inequality constant: " + max);
        float d_col[] = new float[n];
        float d_row[] = new float[n];
        float d_tot = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    d[i][j] += max;
                    d[i][j] = -0.5f * (d[i][j] * d[i][j]);
                    d_col[i] += d[i][j];
                    d_row[j] += d[i][j];
                    d_tot += d[i][j];
                }
            }
        }
        for (int i = 0; i < n; i++) {
            d_col[i] /= n + 0f;
            d_row[i] /= n + 0f;
            d_col[i] *= d_col[i];
            d_row[i] *= d_row[i];
        }
        d_tot /= n * n + 0f;
        d_tot *= d_tot;
        float d_tot2 = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                d[i][j] = d[i][j] - d_col[i] - d_row[j] + d_tot;
            }
        }
        long st = System.currentTimeMillis();
        EigenValueDecomposition evd = new EigenValueDecomposition(d);
        long et = System.currentTimeMillis();
        double[] eig = evd.d;
        double[] eigs = new double[npcs];
        for (int i = 0; i < npcs; i++) {
            eigs[i] = eig[n - 1 - i];
            System.out.println(eigs[i]);
        }
        eigenValues = eigs;
        st = System.currentTimeMillis();
        double[][] d2 = solveEigenvectors(d, eigs);
        System.out.println(d2.length + " " + d2[0].length + " " + eigs.length);
        double[][] d3 = scaleEigenvectors(d2, eigs);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < npcs; j++) {
                int jj = eig.length - j - 1;
                out[i][j] = (float) (d3[j][i]);
            }
        }
    }
