    public CalculateHopkinsStatistic(float[][] data, int resamples, int type) {
        this.resamples = resamples;
        this.type = type;
        int dims = data[0].length;
        int n = data.length;
        float[][] inputData = new float[dims][n];
        BasicStatistics bs = new BasicStatistics();
        for (int i = 0; i < dims; i++) {
            for (int j = 0; j < n; j++) {
                inputData[i][j] = data[j][i];
            }
            float av = (float) bs.calculateMean(inputData[i]);
            for (int j = 0; j < n; j++) {
                inputData[i][j] -= av;
            }
        }
        double[] sds = new double[dims];
        for (int i = 0; i < dims; i++) {
            sds[i] = bs.calculateSD(inputData[i], true);
        }
        for (int i = 0; i < picks.length; i++) {
            if (picks[i] >= n - 1) {
                picks[i] = n - 2;
            }
        }
        double realscore[] = calculateSumNNearestNeighbour(inputData, picks);
        float[][] simData = new float[dims][n];
        double[][] simresults = new double[picks.length][resamples];
        silhouetteMax = new double[n][resamples];
        for (int i = 0; i < resamples; i++) {
            for (int j = 0; j < dims; j++) {
                for (int k = 0; k < n; k++) {
                    simData[j][k] = (float) (random.nextGaussian() * sds[j]);
                }
            }
            double[] score = calculateSumNNearestNeighbour(simData, inputData, picks);
            for (int j = 0; j < picks.length; j++) {
                simresults[j][i] = score[j] / (score[j] + realscore[j]);
            }
        }
        for (int i = 0; i < n; i++) {
            Arrays.sort(silhouetteMax[i]);
        }
        resultString = new String[picks.length];
        results = new double[picks.length][4];
        for (int i = 0; i < picks.length; i++) {
            double meanscore = bs.calculateMean(simresults[i]);
            double sdscore = bs.calculateSD(simresults[i], true);
            double upper2point5 = bs.calculatePercentile(simresults[i], 2.5, true);
            double lower2point5 = bs.calculatePercentile(simresults[i], 2.5, false);
            results[i][0] = meanscore;
            results[i][1] = sdscore;
            results[i][2] = upper2point5;
            results[i][3] = lower2point5;
            resultString[i] = picks[i] + " MEAN: " + meanscore + " SD: " + sdscore + " UPPER 2.5: " + upper2point5 + " LOWER 2.5: " + lower2point5;
        }
    }
