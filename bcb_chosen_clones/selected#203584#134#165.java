    public static double[][] nextWishart(double df, double[][] scaleMatrix) {
        int dim = scaleMatrix.length;
        double[][] draw = new double[dim][dim];
        double[][] z = new double[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < i; j++) {
                z[i][j] = MathUtils.nextGaussian();
            }
        }
        for (int i = 0; i < dim; i++) z[i][i] = Math.sqrt(MathUtils.nextGamma((df - i) * 0.5, 0.5));
        double[][] cholesky = new double[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = i; j < dim; j++) cholesky[i][j] = cholesky[j][i] = scaleMatrix[i][j];
        }
        try {
            cholesky = (new CholeskyDecomposition(cholesky)).getL();
        } catch (IllegalDimension illegalDimension) {
            throw new RuntimeException("Numerical exception in WishartDistribution");
        }
        double[][] result = new double[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                for (int k = 0; k < dim; k++) result[i][j] += cholesky[i][k] * z[k][j];
            }
        }
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                for (int k = 0; k < dim; k++) draw[i][j] += result[i][k] * result[j][k];
            }
        }
        return draw;
    }
