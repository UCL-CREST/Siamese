    double[][] transpose(double[][] d) {
        double[][] r = new double[d[0].length][d.length];
        for (int i = 0; i < d.length; i++) for (int j = 0; j < d[0].length; j++) r[j][i] = d[i][j];
        return r;
    }
