    static double[][] transpose(double[][] xs) {
        double[][] ys = new double[xs[0].length][xs.length];
        for (int i = 0; i < xs.length; ++i) for (int j = 0; j < xs[i].length; ++j) ys[j][i] = xs[i][j];
        return ys;
    }
