    public void greatestIncrease(int maxIterations) {
        double[] increase = new double[numModels];
        int[] id = new int[numModels];
        Model md = new Model();
        double oldPerf = 1;
        for (int i = 0; i < numModels; i++) {
            md.addModel(models[i], false);
            increase[i] = oldPerf - md.getLoss();
            id[i] = i;
            oldPerf = md.getLoss();
        }
        for (int i = 0; i < numModels; i++) {
            for (int j = 0; j < numModels - 1 - i; j++) {
                if (increase[j] < increase[j + 1]) {
                    double increasetemp = increase[j];
                    int temp = id[j];
                    increase[j] = increase[j + 1];
                    id[j] = id[j + 1];
                    increase[j + 1] = increasetemp;
                    id[j + 1] = temp;
                }
            }
        }
        for (int i = 0; i < maxIterations; i++) {
            addToEnsemble(models[id[i]]);
            if (report) ensemble.report(models[id[i]].getName(), allSets);
            updateBestModel();
        }
    }
