    private static double[][] makeAutoCovarianceMatrice_(double[][] vec) {
        int dim = vec[0].length;
        double[][] out = new double[dim][dim];
        double _n = 1. / vec.length;
        for (int k = 0; k < vec.length; k++) {
            double[] x = vec[k];
            for (int i = 0; i < dim; i++) for (int j = i; j < dim; j++) out[i][j] += x[i] * x[j];
        }
        for (int i = 0; i < dim; i++) for (int j = i; j < dim; j++) out[i][j] *= _n;
        for (int i = 0; i < dim; i++) for (int j = i; j < dim; j++) out[j][i] = out[i][j];
        return out;
    }
