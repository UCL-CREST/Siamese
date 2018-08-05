    static double[][] transpose(final double a[][]) {
        int nc = 0;
        for (double[] v : a) {
            if (v.length > nc) nc = v.length;
        }
        double[][] b = new double[nc][];
        for (int i = 0; i < b.length; i++) b[i] = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                b[j][i] = a[i][j];
            }
        }
        return b;
    }
