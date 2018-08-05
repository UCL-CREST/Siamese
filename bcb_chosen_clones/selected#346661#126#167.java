    protected double calculateLogLikelihood(double[][] X, double[] Y, Matrix jacobian, double[] deltas) {
        double LL = 0;
        double[][] Arr = new double[jacobian.rows][jacobian.rows];
        for (int j = 0; j < Arr.length; j++) {
            for (int k = 0; k < Arr.length; k++) {
                Arr[j][k] = 0;
            }
            deltas[j] = 0;
        }
        for (int i = 0; i < X.length; i++) {
            double p = evaluateProbability(X[i]);
            if (Y[i] == 1) {
                LL = LL - 2 * Math.log(p);
            } else {
                LL = LL - 2 * Math.log(1 - p);
            }
            double w = p * (1 - p);
            double z = (Y[i] - p);
            for (int j = 0; j < Arr.length; j++) {
                double xij = X[i][j];
                deltas[j] += xij * z;
                for (int k = j; k < Arr.length; k++) {
                    Arr[j][k] += xij * X[i][k] * w;
                }
            }
        }
        for (int j = 0; j < m_Par.length; j++) {
            deltas[j] -= 2 * m_Ridge * m_Par[j];
        }
        for (int j = 0; j < Arr.length; j++) {
            Arr[j][j] += 2 * m_Ridge;
        }
        for (int j = 1; j < Arr.length; j++) {
            for (int k = 0; k < j; k++) {
                Arr[j][k] = Arr[k][j];
            }
        }
        for (int j = 0; j < Arr.length; j++) {
            jacobian.setRow(j, Arr[j]);
        }
        return LL;
    }
