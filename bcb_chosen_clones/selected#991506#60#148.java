    public int solve() {
        Date startTime = new Date();
        sensorDistances = new double[sensors.length][sensors.length];
        for (int s = 0; s < sensors.length; s++) {
            for (int t = s; t < sensors.length; t++) {
                sensorDistances[s][t] = sensors[s].getDistance(sensors[t]);
                sensorDistances[t][s] = sensorDistances[s][t];
            }
        }
        int solutionCount = sensors[0].getNumberEvents() * sensors[1].getNumberEvents() * sensors[2].getNumberEvents();
        solutionError = new double[solutionCount];
        possibleSolutions = new NoiseEvent[solutionCount];
        solutionEvents = new int[solutionCount][];
        int[] order = new int[solutionCount];
        int[] triplet = new int[3];
        for (int i = 0; i < 3; i++) {
            triplet[i] = 0;
        }
        double[][] xx = new double[sensors.length - 2][];
        double[][] yy = new double[sensors.length - 2][];
        double[][] tt = new double[sensors.length - 2][];
        for (int i = sensors.length - 3; i >= 0; i--) {
            xx[i] = new double[i + 3];
            yy[i] = new double[i + 3];
            tt[i] = new double[i + 3];
        }
        int solutions = 0;
        while (triplet[0] < sensors[0].getNumberEvents()) {
            int numSensors = 3;
            double[] x = xx[numSensors - 3];
            double[] y = yy[numSensors - 3];
            double[] t = tt[numSensors - 3];
            for (int s = 0; s < 3; s++) {
                x[s] = sensors[s].getX();
                y[s] = sensors[s].getY();
                t[s] = sensors[s].getEvent(triplet[s]);
            }
            NoiseEvent ne = new NoiseEvent(x, y, t, getTemperatureAtTime(t[0]));
            boolean possibleNE = ne.possibleNoiseByDistance();
            double solFitness = Double.POSITIVE_INFINITY;
            if (possibleNE) {
                ne.solve();
                solFitness = ne.getSolutionFitness();
                int[] tripletCopy = new int[triplet.length];
                for (int i = 0; i < triplet.length; i++) {
                    tripletCopy[i] = triplet[i];
                }
                solutionEvents[solutions] = tripletCopy;
                possibleSolutions[solutions] = ne;
                solutionError[solutions] = solFitness;
                order[solutions] = solutions;
                solutions++;
            }
            nextPossibleNoise(triplet);
        }
        double[] se = new double[solutions];
        for (int s = 0; s < solutions; s++) {
            se[s] = solutionError[s];
        }
        solutionError = null;
        solutionError = se;
        int[] o = new int[solutions];
        for (int s = 0; s < solutions; s++) {
            o[s] = order[s];
        }
        order = null;
        order = o;
        int[][] sev = new int[solutions][];
        for (int s = 0; s < solutions; s++) {
            sev[s] = solutionEvents[s];
        }
        solutionEvents = null;
        solutionEvents = sev;
        NoiseEvent[] ne = new NoiseEvent[solutions];
        for (int s = 0; s < solutions; s++) {
            ne[s] = possibleSolutions[s];
        }
        possibleSolutions = null;
        possibleSolutions = ne;
        BubbleSort bs = new BubbleSort(false);
        bs.sort(order, solutionError);
        System.out.println();
        for (int i = 0; i < solutions; i++) {
            System.out.println(solutionEvents[order[i]][0] + "," + solutionEvents[order[i]][1] + "," + solutionEvents[order[i]][2] + "," + solutionError[order[i]] + "," + possibleSolutions[order[i]].getSolutionX() + "," + possibleSolutions[order[i]].getSolutionY() + "," + possibleSolutions[order[i]].getSolutionTime());
        }
        Date endTime = new Date();
        solveRunTime = (endTime.getTime() - startTime.getTime()) / 1000.0;
        return solutions;
    }
