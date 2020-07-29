    public int solve() {
        Date startTime = new Date();
        int noiseCount = 0;
        for (int s = 0; s < sensors.length; s++) {
            noiseCount += sensors[s].getNumberEvents();
        }
        timeOrderSensor = new int[noiseCount];
        timeOrderEvent = new int[noiseCount];
        int[] checkList = new int[sensors.length];
        int noisesDone = 0;
        int earliestSensor = -1;
        int earliestEvent = -1;
        while (noisesDone < noiseCount) {
            earliestSensor = -1;
            earliestEvent = -1;
            double earliestTime = Double.POSITIVE_INFINITY;
            for (int s = 0; s < sensors.length; s++) {
                if (checkList[s] < sensors[s].getNumberEvents()) {
                    if (sensors[s].getEvent(checkList[s]) < earliestTime) {
                        earliestTime = sensors[s].getEvent(checkList[s]);
                        earliestSensor = s;
                        earliestEvent = checkList[s];
                    }
                }
            }
            timeOrderSensor[noisesDone] = earliestSensor;
            timeOrderEvent[noisesDone] = earliestEvent;
            checkList[earliestSensor]++;
            noisesDone++;
        }
        sensorDistances = new double[sensors.length][sensors.length];
        for (int s = 0; s < sensors.length; s++) {
            for (int t = s; t < sensors.length; t++) {
                sensorDistances[s][t] = sensors[s].getDistance(sensors[t]);
                sensorDistances[t][s] = sensorDistances[s][t];
            }
        }
        int solutionSize = 20;
        int solutions = 0;
        possibleSolutions = new NoiseEvent[20];
        aveError = new double[20];
        sensorError = new double[20][];
        int[] lowPossibleNoise = new int[sensors.length];
        int[] hihPossibleNoise = new int[sensors.length];
        for (int n = 0; n < noisesDone; n++) {
            int homeSensor = timeOrderSensor[n];
            lowPossibleNoise[homeSensor] = timeOrderEvent[n];
            hihPossibleNoise[homeSensor] = timeOrderEvent[n];
            for (int s = 0; s < sensors.length; s++) {
                if (s != homeSensor) {
                    findHighLow(homeSensor, s, timeOrderEvent[n], lowPossibleNoise, hihPossibleNoise);
                }
            }
            int sCount = 0;
            for (int s = 0; s < sensors.length; s++) {
                if (hihPossibleNoise[s] >= lowPossibleNoise[s]) {
                    sCount++;
                }
            }
            if (sCount >= 4) {
                int[] possibleNoise = new int[sensors.length];
                possibleNoise[0] = -2;
                boolean possible = nextPossibleNoise(possibleNoise, lowPossibleNoise, hihPossibleNoise);
                while (possible == true) {
                    double[] x = new double[sCount];
                    double[] y = new double[sCount];
                    double[] t = new double[sCount];
                    int i = 0;
                    for (int s = 0; s < sensors.length; s++) {
                        if (hihPossibleNoise[s] > -1) {
                            x[i] = sensors[s].getX();
                            y[i] = sensors[s].getY();
                            t[i] = sensors[s].getEvent(possibleNoise[s]);
                            i++;
                        }
                    }
                    NoiseEvent ne = new NoiseEvent(x, y, t, getTemperatureAtTime(t[0]));
                    ne.solve();
                    double[] error = new double[sCount];
                    double aveErrorInMeters = ne.distanceFitness(error);
                    double xDif = ne.getSolutionX() - ne.getSensorsCentroidX();
                    double yDif = ne.getSolutionY() - ne.getSensorsCentroidY();
                    double solutionDistanceSquared = xDif * xDif + yDif * yDif;
                    double tDif = ne.getMinimumTimeDifference() - ne.getSolutionTime();
                    if (aveErrorInMeters <= mTolerance && mTimeDiffercial >= tDif && mDistFromSensors * mDistFromSensors >= solutionDistanceSquared) {
                        if (solutions == solutionSize) {
                            solutionSize += 20;
                            NoiseEvent[] oldPossibleSolutions = possibleSolutions;
                            possibleSolutions = new NoiseEvent[solutionSize];
                            double[] oldAveError = aveError;
                            aveError = new double[solutionSize];
                            double[][] oldSensorError = sensorError;
                            sensorError = new double[solutionSize][];
                            for (int old = 0; old < oldPossibleSolutions.length; old++) {
                                possibleSolutions[old] = oldPossibleSolutions[old];
                                aveError[old] = oldAveError[old];
                                sensorError[old] = oldSensorError[old];
                            }
                        }
                        possibleSolutions[solutions] = ne;
                        aveError[solutions] = aveErrorInMeters;
                        sensorError[solutions] = error;
                        solutions++;
                    }
                    possible = nextPossibleNoise(possibleNoise, lowPossibleNoise, hihPossibleNoise);
                }
            }
        }
        if (solutions == 0) {
            possibleSolutions = null;
            aveError = null;
            sensorError = null;
        } else {
            NoiseEvent[] oldPossibleSolutions = possibleSolutions;
            possibleSolutions = new NoiseEvent[solutions];
            double[] oldAveError = aveError;
            aveError = new double[solutions];
            double[][] oldSensorError = sensorError;
            sensorError = new double[solutions][];
            for (int old = 0; old < solutions; old++) {
                possibleSolutions[old] = oldPossibleSolutions[old];
                aveError[old] = oldAveError[old];
                sensorError[old] = oldSensorError[old];
            }
        }
        Date endTime = new Date();
        solveRunTime = (endTime.getTime() - startTime.getTime()) / 1000;
        return solutions;
    }
