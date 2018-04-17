    public static double[][] transpose(double[][] A) {
        if (logger.isDebugEnabled()) {
            debug("Matrix A:", A);
        }
        int _dimension = A.length;
        double[][] B = new double[_dimension][_dimension];
        for (int i = 0; i < _dimension; i++) {
            for (int k = 0; k < _dimension; k++) {
                B[k][i] = A[i][k];
            }
        }
        if (logger.isDebugEnabled()) {
            debug("B = transpose(A):", B);
        }
        return B;
    }
