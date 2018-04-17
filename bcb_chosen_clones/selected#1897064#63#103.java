    public static LDAFunction calcLDA(double[][] features1, double[][] features2) {
        if (features1.length == 0 || features2.length == 0) throw new IllegalArgumentException("Both feature arrays must contain entries");
        int groupCount = 2;
        int featureCount = features1[0].length;
        double[] featMeans = new double[featureCount];
        double[][] groupMeans = new double[groupCount][featureCount];
        for (int i = 0; i < features1.length; i++) {
            for (int j = 0; j < featureCount; j++) {
                groupMeans[0][j] += features1[i][j];
                featMeans[j] += features1[i][j];
            }
        }
        for (int i = 0; i < features2.length; i++) {
            for (int j = 0; j < featureCount; j++) {
                groupMeans[1][j] += features2[i][j];
                featMeans[j] += features2[i][j];
            }
        }
        for (int j = 0; j < featureCount; j++) {
            groupMeans[0][j] /= features1.length;
            groupMeans[1][j] /= features2.length;
            featMeans[j] /= features1.length + features2.length;
        }
        double[][] covariance = new double[featureCount][featureCount];
        for (int i = 0; i < featureCount; i++) {
            for (int j = 0; j < featureCount; j++) {
                if (j >= i) {
                    for (int k = 0; k < features1.length; k++) {
                        covariance[i][j] += (features1[k][i] - featMeans[i]) * (features1[k][j] - featMeans[j]);
                    }
                    for (int k = 0; k < features2.length; k++) {
                        covariance[i][j] += (features2[k][i] - featMeans[i]) * (features2[k][j] - featMeans[j]);
                    }
                    covariance[i][j] /= features1.length + features2.length;
                } else {
                    covariance[i][j] = covariance[j][i];
                }
            }
        }
        return getFunctionFromCovarianceMatrix(covariance, groupMeans);
    }
