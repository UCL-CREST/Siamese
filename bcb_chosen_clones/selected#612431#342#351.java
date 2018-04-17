    public static double[][] getCorrelations(double[][] v) {
        double[][] c = new double[v.length][v.length];
        for (int i = 0; i < v.length - 1; i++) {
            for (int j = i + 1; j < v.length; j++) {
                c[i][j] = Pearson.calcCorrelation(v[i], v[j]);
                c[j][i] = c[i][j];
            }
        }
        return c;
    }
