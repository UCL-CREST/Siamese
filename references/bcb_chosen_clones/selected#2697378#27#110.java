    @Override
    public double[] aggregatePredictions(BasePredictions bp) throws Exception {
        ExtInstances trainingSet = bp.getDecomposedDatasetLearner().getTrainingSet();
        int numOfClasses = bp.getNumClasses();
        if (numOfInstancesPerClass == null || lastTrainingSet != trainingSet) {
            numOfInstancesPerClass = new int[numOfClasses];
            for (ExtInstance instance : trainingSet.get_Instances()) {
                numOfInstancesPerClass[bp.getClassIndex((instance.getM_Classes().get(0)))]++;
            }
            n = new int[numOfClasses][numOfClasses];
            for (int i = 0; i < numOfClasses; i++) {
                for (int j = i + 1; j < numOfClasses; j++) {
                    n[i][j] = numOfInstancesPerClass[i] + numOfInstancesPerClass[j];
                    n[j][i] = n[i][j];
                }
            }
            lastTrainingSet = trainingSet;
        }
        Random random = new Random(Configuration.getInstance().seedForEvaluation);
        double[] probabilities = new double[numOfClasses];
        double[][] my = new double[numOfClasses][numOfClasses];
        double sumOfProbabilities = 0;
        for (int j = 0; j < numOfClasses; j++) {
            probabilities[j] = random.nextDouble();
            sumOfProbabilities += probabilities[j];
        }
        for (int j = 0; j < probabilities.length; j++) {
            probabilities[j] = probabilities[j] / sumOfProbabilities;
        }
        for (int j = 0; j < my.length; j++) {
            for (int k = j + 1; k < my.length; k++) {
                double tmpSum = probabilities[j] + probabilities[k];
                my[j][k] = probabilities[j] / tmpSum;
                my[k][j] = 1 - my[j][k];
            }
        }
        double[][] r = new double[numOfClasses][numOfClasses];
        int numBaseClassifiers = bp.getNumPredictions();
        for (int j = 0; j < numBaseClassifiers; j++) {
            double[] distrib = bp.getPrediction(j);
            String[] predictedClasses = bp.getVersusClasses(j);
            int indexOfClass1 = bp.getClassIndex(predictedClasses[0]);
            int indexOfClass2 = bp.getClassIndex(predictedClasses[1]);
            r[indexOfClass1][indexOfClass2] = distrib[0];
            r[indexOfClass2][indexOfClass1] = distrib[1];
        }
        double[] alpha = new double[numOfClasses];
        int c_i = 0;
        while (true) {
            double sumOfnAndr = 0;
            double sumOfnAndmy = 0;
            for (int c_j = 0; c_j < numOfClasses; c_j++) {
                if (c_i != c_j) {
                    sumOfnAndr += r[c_i][c_j] * n[c_i][c_j];
                    sumOfnAndmy += my[c_i][c_j] * n[c_i][c_j];
                }
            }
            alpha[c_i] = sumOfnAndr / sumOfnAndmy;
            for (int c_j = 0; c_j < numOfClasses; c_j++) {
                if (c_i != c_j) {
                    double alphaMy = alpha[c_i] * my[c_i][c_j];
                    double sumOfMy = alphaMy + my[c_j][c_i];
                    my[c_i][c_j] = alphaMy / sumOfMy;
                    my[c_j][c_i] = 1 - my[c_i][c_j];
                }
            }
            probabilities[c_i] = alpha[c_i] * probabilities[c_i];
            if (checkBreakCondition(alpha)) {
                break;
            }
            c_i++;
            if (c_i >= numOfClasses) {
                c_i = 0;
            }
        }
        sumOfProbabilities = 0;
        for (double p_i : probabilities) {
            sumOfProbabilities += p_i;
        }
        for (int j = 0; j < probabilities.length; j++) {
            probabilities[j] = probabilities[j] / sumOfProbabilities;
        }
        return probabilities;
    }
