    public void computeForwardDensity(double[] outMean, double[][] outVariance, double[][] outPrecision) {
        double[] W = linearModel.getTransformedDependentParameter();
        double[] P = linearModel.getScale();
        for (int k = 0; k < numEffects; k++) {
            if (k != effectNumber) {
                double[] thisXBeta = linearModel.getXBeta(k);
                for (int i = 0; i < N; i++) W[i] -= thisXBeta[i];
            }
        }
        double[] priorBetaMean = effectPrior.getMean();
        double[][] priorBetaScale = effectPrior.getScaleMatrix();
        double[][] XtP = new double[dim][N];
        for (int j = 0; j < dim; j++) {
            if (hasNoIndicators || indicators.getParameterValue(j) == 1) {
                for (int i = 0; i < N; i++) XtP[j][i] = X[i][j] * P[i];
            }
        }
        double[][] XtPX = new double[dim][dim];
        for (int i = 0; i < dim; i++) {
            if (hasNoIndicators || indicators.getParameterValue(i) == 1) {
                for (int j = i; j < dim; j++) {
                    if (hasNoIndicators || indicators.getParameterValue(j) == 1) {
                        for (int k = 0; k < N; k++) XtPX[i][j] += XtP[i][k] * X[k][j];
                        XtPX[j][i] = XtPX[i][j];
                    }
                }
            }
        }
        double[][] XtPX_plus_P0 = new double[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = i; j < dim; j++) XtPX_plus_P0[j][i] = XtPX_plus_P0[i][j] = XtPX[i][j] + priorBetaScale[i][j];
        }
        double[] XtPW = new double[dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < N; j++) XtPW[i] += XtP[i][j] * W[j];
        }
        double[] P0Mean0 = new double[dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) P0Mean0[i] += priorBetaScale[i][j] * priorBetaMean[j];
        }
        double[] unscaledMean = new double[dim];
        for (int i = 0; i < dim; i++) unscaledMean[i] = P0Mean0[i] + XtPW[i];
        double[][] variance = new SymmetricMatrix(XtPX_plus_P0).inverse().toComponents();
        for (int i = 0; i < dim; i++) {
            outMean[i] = 0.0;
            for (int j = 0; j < dim; j++) {
                outMean[i] += variance[i][j] * unscaledMean[j];
                outVariance[i][j] = variance[i][j];
                outPrecision[i][j] = XtPX_plus_P0[i][j];
            }
        }
    }
