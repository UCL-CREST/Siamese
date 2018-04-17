    public static LDAFunction calcLDA(int[] groupNo, double[][] features) {
        if (groupNo.length != features.length) throw new IllegalArgumentException("Number of items in groupNo must be same as features!");
        if (groupNo.length == 0) throw new IllegalArgumentException("groupNo.length == 0");
        for (int i = 0; i < groupNo.length; i++) {
            if (groupNo[i] != 0 && groupNo[i] != 1) throw new IllegalArgumentException("Only instances with 2 groups allowed!");
        }
        int groupCount = 2;
        int featureCount = features[0].length;
        double[] featMeans = new double[featureCount];
        double[][] groupMeans = new double[groupCount][featureCount];
        int[] groupN = new int[groupCount];
        for (int i = 0; i < groupNo.length; i++) {
            for (int j = 0; j < featureCount; j++) {
                featMeans[j] += features[i][j];
                groupMeans[groupNo[i]][j] += features[i][j];
            }
            groupN[groupNo[i]]++;
        }
        for (int j = 0; j < featureCount; j++) {
            featMeans[j] /= groupNo.length;
            for (int i = 0; i < groupCount; i++) {
                groupMeans[i][j] /= groupN[i];
            }
        }
        double[][] covariance = new double[featureCount][featureCount];
        for (int i = 0; i < featureCount; i++) {
            for (int j = 0; j < featureCount; j++) {
                if (j >= i) {
                    for (int k = 0; k < groupNo.length; k++) {
                        covariance[i][j] += (features[k][i] - featMeans[i]) * (features[k][j] - featMeans[j]);
                    }
                    covariance[i][j] /= groupNo.length;
                } else {
                    covariance[i][j] = covariance[j][i];
                }
            }
        }
        return getFunctionFromCovarianceMatrix(covariance, groupMeans);
    }
