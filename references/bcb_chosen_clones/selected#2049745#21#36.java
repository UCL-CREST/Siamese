    protected double[][] computeCrossCorrelationsMatrix(int size) {
        double[][] m = new double[size][size];
        SpecUnit x, y;
        int pom;
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j <= i; j++) {
                if (i == j) {
                    m[i][j] = 1;
                } else {
                    m[i][j] = computeCorrelation((SpecUnit) mapUnits.get(i), (SpecUnit) mapUnits.get(j));
                    m[j][i] = m[i][j];
                }
            }
        }
        return m;
    }
