    public SyntaxAnalysis(float[][] input, int[][] lookUps, boolean waste) {
        this.nSongs = nSongs;
        n = input.length;
        setUpNext(lookUps);
        m = 0;
        for (int i = 0; i < n; i++) {
            if (!hasPrevious[i]) {
                m++;
            }
        }
        n2 = n - m;
        data = new float[n][n];
        float[][] reald2 = new float[n][n];
        float[][] simd2 = new float[n][n];
        float[][] maxd2 = new float[n][n];
        float[][] maxd = new float[n][n];
        float[][] mind2 = new float[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                data[i][j] = input[i][j];
                data[j][i] = input[i][j];
                mind2[i][j] = 1000000f;
            }
        }
        float[][] si1 = new float[n2][];
        for (int i = 0; i < n2; i++) {
            si1[i] = new float[i + 1];
        }
        int[] order = new int[n];
        getStraightOrder(order);
        syntaxCompression2(si1, order);
        double[] realResult = calculateMeanKNearestNeighbour(si1, locs);
        double[] perpointreal = syntaxComparison2(order, 5);
        double[] perpointsim;
        int p = locs.length;
        double[][] simResults = new double[p][nrepeats];
        int[] perpointcount = new int[n2];
        for (int i = 0; i < nrepeats; i++) {
            reorder(order);
            syntaxCompression2(si1, order);
            double[] sim = calculateMeanKNearestNeighbour(si1, locs);
            for (int j = 0; j < p; j++) {
                simResults[j][i] = sim[j] / (sim[j] + realResult[j]);
            }
            perpointsim = syntaxComparison2(order, 5);
            for (int j = 0; j < n2; j++) {
                if (perpointsim[j] > perpointreal[j]) {
                    perpointcount[j]++;
                }
            }
        }
        double mincount = 10000;
        double maxcount = 0;
        for (int i = 0; i < n2; i++) {
            if (perpointcount[i] > maxcount) {
                maxcount = perpointcount[i];
            }
            if (perpointcount[i] < mincount) {
                mincount = perpointcount[i];
            }
        }
        transLabels = new double[n];
        for (int i = 0; i < n2; i++) {
            transLabels[i] = perpointcount[i] / (nrepeats + 0.0);
        }
        BasicStatistics bs = new BasicStatistics();
        double[] mean = new double[p];
        double[] sd = new double[p];
        double[] upper = new double[p];
        double[] lower = new double[p];
        resultString = new String[p];
        for (int i = 0; i < p; i++) {
            mean[i] = bs.calculateMean(simResults[i]);
            sd[i] = bs.calculateSD(simResults[i], true);
            upper[i] = bs.calculatePercentile(simResults[i], 2.5, true);
            lower[i] = bs.calculatePercentile(simResults[i], 2.5, false);
            resultString[i] = "SYNTAX ANAL: " + mean[i] + " " + sd[i] + " " + upper[i] + " " + lower[i];
        }
        si1 = null;
    }
