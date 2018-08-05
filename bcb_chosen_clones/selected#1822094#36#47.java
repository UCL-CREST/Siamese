    private static double[][] createCostMatrix(SimplePoint2d[] cities) {
        final int noCity = cities.length;
        double[][] dMat = new double[noCity][noCity];
        for (int i = 0; i < noCity; i++) {
            dMat[i][i] = 0.0;
            for (int j = i + 1; j < noCity; j++) {
                dMat[i][j] = SimplePoint2d.distance(cities[i], cities[j]);
                dMat[j][i] = dMat[i][j];
            }
        }
        return dMat;
    }
