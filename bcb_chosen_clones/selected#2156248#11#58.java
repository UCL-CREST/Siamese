    public double[] fit(double[] x, double[] y, double[] sigmaX, double[] sigmaY, int order) {
        final double parameters[] = new double[2 * order + 2];
        final int numOfPoints = x.length;
        final int nk = parameters.length / 2;
        double[][] alpha = new double[nk][nk];
        double[] beta = new double[nk];
        double term = 0;
        double product1 = 1.0f, product2 = 1.0f;
        for (int k = 0; k < nk; k++) {
            for (int j = k; j < nk; j++) {
                term = 0.0f;
                alpha[k][j] = 0.0f;
                for (int i = 0; i < numOfPoints; i++) {
                    product1 = 1.0f;
                    if (k > 0) for (int m = 0; m < k; m++) product1 *= x[i];
                    product2 = 1.0f;
                    if (j > 0) for (int m = 0; m < j; m++) product2 *= x[i];
                    term = (product1 * product2);
                    if (sigmaY != null && sigmaY[i] != 0.0f) term /= (sigmaY[i] * sigmaY[i]);
                    alpha[k][j] += term;
                }
                alpha[j][k] = alpha[k][j];
            }
            for (int i = 0; i < numOfPoints; i++) {
                product1 = 1.0f;
                if (k > 0) for (int m = 0; m < k; m++) product1 *= x[i];
                term = (y[i] * product1);
                if (sigmaY != null && sigmaY[i] != 0.0) term /= (sigmaY[i] * sigmaY[i]);
                beta[k] += term;
            }
        }
        Matrix alphaMatrix = new Matrix(alpha);
        QRDecomposition alphaQRDecomposition = new QRDecomposition(alphaMatrix);
        Matrix betaMatrix = new Matrix(beta, nk);
        Matrix parameterMatrix;
        try {
            parameterMatrix = alphaQRDecomposition.solve(betaMatrix);
        } catch (Exception e) {
            e.printStackTrace();
            return new double[0];
        }
        Matrix covarianceMatrix = alphaMatrix.inverse();
        for (int k = 0; k < nk; k++) {
            parameters[k] = parameterMatrix.get(k, 0);
            parameters[k + nk] = Math.sqrt(covarianceMatrix.get(k, k));
        }
        return parameters;
    }
