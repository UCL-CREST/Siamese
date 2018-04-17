    @Override
    public double[] aggregatePredictions(BasePredictions bp) throws Exception {
        int numClasses = bp.getNumClasses();
        double[] p = new double[numClasses];
        double[][] r = new double[numClasses][numClasses];
        int numBaseClassifiers = bp.getNumPredictions();
        for (int i = 0; i < numBaseClassifiers; i++) {
            double[] distrib = bp.getPrediction(i);
            String[] predictedClasses = bp.getVersusClasses(i);
            int indexOfClass1 = bp.getClassIndex(predictedClasses[0]);
            int indexOfClass2 = bp.getClassIndex(predictedClasses[1]);
            r[indexOfClass1][indexOfClass2] = distrib[0];
            r[indexOfClass2][indexOfClass1] = distrib[1];
        }
        int t, j;
        int k = numClasses;
        int iter = 0;
        int max_iter = Math.max(Configuration.getInstance().WLWmaxIterations, k);
        double[][] Q = new double[k][k];
        double[] Qp = new double[k];
        double pQp;
        double eps = Configuration.getInstance().WLWepsilon / k;
        for (t = 0; t < k; t++) {
            p[t] = 1.0 / k;
            Q[t][t] = 0;
            for (j = 0; j < t; j++) {
                Q[t][t] += r[j][t] * r[j][t];
                Q[t][j] = Q[j][t];
            }
            for (j = t + 1; j < k; j++) {
                Q[t][t] += r[j][t] * r[j][t];
                Q[t][j] = -r[j][t] * r[t][j];
            }
        }
        for (iter = 0; iter < max_iter; iter++) {
            pQp = 0;
            for (t = 0; t < k; t++) {
                Qp[t] = 0;
                for (j = 0; j < k; j++) Qp[t] += Q[t][j] * p[j];
                pQp += p[t] * Qp[t];
            }
            double max_error = 0;
            for (t = 0; t < k; t++) {
                double error = Math.abs(Qp[t] - pQp);
                if (error > max_error) max_error = error;
            }
            if (max_error < eps) break;
            for (t = 0; t < k; t++) {
                double diff = (-Qp[t] + pQp) / Q[t][t];
                p[t] += diff;
                pQp = (pQp + diff * (diff * Q[t][t] + 2 * Qp[t])) / (1 + diff) / (1 + diff);
                for (j = 0; j < k; j++) {
                    Qp[j] = (Qp[j] + diff * Q[t][j]) / (1 + diff);
                    p[j] /= (1 + diff);
                }
            }
        }
        if (iter >= max_iter) {
            throw new Exception("Maximal number of iterations reached!");
        }
        return p;
    }
