    public static boolean[][] randomGraph(int n, double p, Random random) {
        if (n <= 0) return null;
        boolean g[][] = new boolean[n][n];
        if (p > 1.0) p = 1.0;
        if (p < 0.0) p = 0.0;
        for (int i = 0; i < n; i++) {
            g[i][i] = false;
            for (int j = i + 1; j < n; j++) {
                g[i][j] = (random.nextDouble() <= p);
                g[j][i] = g[i][j];
            }
        }
        return g;
    }
