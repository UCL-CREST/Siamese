    public static boolean[][] randomForest(int n, double p, Random random) {
        if (n <= 0) return null;
        boolean g[][] = new boolean[n][n];
        if (p > 1.0) p = 1.0;
        if (p < 0.0) p = 0.0;
        int connectedComponent[] = new int[n];
        for (int i = 0; i < n; i++) connectedComponent[i] = i;
        for (int i = 0; i < n; i++) {
            g[i][i] = false;
            for (int j = i + 1; j < n; j++) {
                if (connectedComponent[i] == connectedComponent[j]) {
                    g[i][j] = false;
                    g[j][i] = false;
                } else {
                    g[i][j] = (random.nextDouble() <= p);
                    g[j][i] = g[i][j];
                    if (g[i][j]) {
                        int oldComponent = connectedComponent[j];
                        for (int k = 0; k < n; k++) if (connectedComponent[k] == oldComponent) connectedComponent[k] = connectedComponent[i];
                    }
                }
            }
        }
        return g;
    }
