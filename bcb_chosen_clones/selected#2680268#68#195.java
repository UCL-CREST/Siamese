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
        int solutions = 0;
        while (triplet[0] < sensors[0].getNumberEvents()) {
            int numSensors = 3;
            double[] x = new double[numSensors];
            double[] y = new double[numSensors];
            double[] t = new double[numSensors];
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
        for (int ps = 0; ps < solutions; ps++) {
            for (int numSensors = 4; numSensors <= sensors.length; numSensors++) {
                double[] x = new double[numSensors];
                double[] y = new double[numSensors];
                double[] t = new double[numSensors];
                for (int s = 0; s < solutionEvents[ps].length; s++) {
                    x[s] = sensors[s].getX();
                    y[s] = sensors[s].getY();
                    t[s] = sensors[s].getEvent(solutionEvents[ps][s]);
                }
                double bestSolFitness = Double.POSITIVE_INFINITY;
                int bestSolEvent = -1;
                for (int lastEvent = 0; lastEvent < sensors[numSensors - 1].getNumberEvents(); lastEvent++) {
                    x[numSensors - 1] = sensors[numSensors - 1].getX();
                    y[numSensors - 1] = sensors[numSensors - 1].getY();
                    t[numSensors - 1] = sensors[numSensors - 1].getEvent(lastEvent);
                    NoiseEvent nne = new NoiseEvent(x, y, t, getTemperatureAtTime(t[0]));
                    boolean possibleNE = nne.possibleNoiseByDistance();
                    if (possibleNE) {
                        nne.solve();
                        double newFitness = nne.getSolutionFitness();
                        if (newFitness < bestSolFitness) {
                            bestSolEvent = lastEvent;
                            bestSolFitness = newFitness;
                        }
                    }
                }
                int[] newSE = new int[numSensors];
                for (int i = 0; i < numSensors - 1; i++) {
                    newSE[i] = solutionEvents[ps][i];
                }
                newSE[numSensors - 1] = bestSolEvent;
                solutionEvents[ps] = newSE;
                solutionError[ps] = bestSolFitness;
                if (bestSolEvent == -1) {
                    numSensors = sensors.length;
                }
            }
        }
        bs.sort(order, solutionError);
        double[] orderedSolutionError = new double[solutions];
        NoiseEvent[] orderedPossibleSolutions = new NoiseEvent[solutions];
        int[][] orderedSolutionEvents = new int[solutions][];
        for (int i = 0; i < solutions; i++) {
            orderedPossibleSolutions[i] = possibleSolutions[order[i]];
            orderedSolutionEvents[i] = solutionEvents[order[i]];
            orderedSolutionError[i] = solutionError[order[i]];
        }
        possibleSolutions = orderedPossibleSolutions;
        solutionEvents = orderedSolutionEvents;
        solutionError = orderedSolutionError;
        Date endTime = new Date();
        solveRunTime = (endTime.getTime() - startTime.getTime()) / 1000.0;
        return solutions;
    }
