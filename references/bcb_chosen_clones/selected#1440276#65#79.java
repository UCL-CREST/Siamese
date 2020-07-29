    public static double[][] Spearman(double[][] X) {
        double[][] output = new double[X.length][X.length];
        try {
            for (int i = 0; i < X.length; i++) {
                output[i][i] = 1.0;
                for (int j = 0; j < i; j++) {
                    output[i][j] = GeneUtils.spearmanrho(X[i], X[j]);
                    output[j][i] = output[i][j];
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Statistic.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }
