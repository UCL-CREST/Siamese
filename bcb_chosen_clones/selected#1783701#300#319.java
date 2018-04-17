    public static void printModEffectOnTargetRegressionError(double[][] tval, double[] mval) {
        int[] rank = ArrayUtils.getRankOrderedIndexes(mval);
        double[][][] c = new double[tval.length][tval.length][];
        for (int i = 0; i < c.length - 1; i++) {
            for (int j = i + 1; j < c.length; j++) {
                c[i][j] = LinearRegression.regress(tval[i], tval[j]);
                c[j][i] = c[i][j];
            }
        }
        int index = 0;
        for (int k : rank) {
            double error = 0;
            for (int i = 0; i < c.length - 1; i++) {
                for (int j = i + 1; j < c.length; j++) {
                    error += LinearRegression.getDistanceOfPoint(1, 0, tval[i][k], tval[j][k]);
                }
            }
            System.out.println((++index) + "\t" + error);
        }
    }
