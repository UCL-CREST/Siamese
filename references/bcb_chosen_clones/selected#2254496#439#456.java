    private static double[][] getCorrelationMatrix(Gene[] genes, int[] sign, boolean[] pos) {
        double[][] correlation = new double[genes.length][genes.length];
        int expSize = pos == null ? genes[0].value.length : ArrayUtils.countTrue(pos);
        for (int i = 0; i < genes.length - 1; i++) {
            for (int j = i + 1; j < genes.length; j++) {
                double[][] val = new double[2][];
                val[0] = genes[i].value;
                val[1] = genes[j].value;
                double cor = pos == null ? Pearson.calcCorrelation(val) : Pearson.calcCorrelation(val, pos);
                double pv = Pearson.calcCorrSignificance(cor, expSize);
                if (pv < CORR_PV_THR) {
                    correlation[i][j] = cor * sign[i] * sign[j];
                    correlation[j][i] = correlation[i][j];
                }
            }
        }
        return correlation;
    }
