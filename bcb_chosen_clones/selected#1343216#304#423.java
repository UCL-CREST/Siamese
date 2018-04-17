    void neighborJoining(int nClusters, Vector<Integer>[] nClusterID, Node[] clusterNodes) {
        int n = m_instances.numInstances();
        double[][] fDist = new double[nClusters][nClusters];
        for (int i = 0; i < nClusters; i++) {
            fDist[i][i] = 0;
            for (int j = i + 1; j < nClusters; j++) {
                fDist[i][j] = getDistance0(nClusterID[i], nClusterID[j]);
                fDist[j][i] = fDist[i][j];
            }
        }
        double[] fSeparationSums = new double[n];
        double[] fSeparations = new double[n];
        int[] nNextActive = new int[n];
        for (int i = 0; i < n; i++) {
            double fSum = 0;
            for (int j = 0; j < n; j++) {
                fSum += fDist[i][j];
            }
            fSeparationSums[i] = fSum;
            fSeparations[i] = fSum / (nClusters - 2);
            nNextActive[i] = i + 1;
        }
        while (nClusters > 2) {
            int iMin1 = -1;
            int iMin2 = -1;
            double fMin = Double.MAX_VALUE;
            if (m_bDebug) {
                for (int i = 0; i < n; i++) {
                    if (nClusterID[i].size() > 0) {
                        double[] fRow = fDist[i];
                        double fSep1 = fSeparations[i];
                        for (int j = 0; j < n; j++) {
                            if (nClusterID[j].size() > 0 && i != j) {
                                double fSep2 = fSeparations[j];
                                double fVal = fRow[j] - fSep1 - fSep2;
                                if (fVal < fMin) {
                                    iMin1 = i;
                                    iMin2 = j;
                                    fMin = fVal;
                                }
                            }
                        }
                    }
                }
            } else {
                int i = 0;
                while (i < n) {
                    double fSep1 = fSeparations[i];
                    double[] fRow = fDist[i];
                    int j = nNextActive[i];
                    while (j < n) {
                        double fSep2 = fSeparations[j];
                        double fVal = fRow[j] - fSep1 - fSep2;
                        if (fVal < fMin) {
                            iMin1 = i;
                            iMin2 = j;
                            fMin = fVal;
                        }
                        j = nNextActive[j];
                    }
                    i = nNextActive[i];
                }
            }
            double fMinDistance = fDist[iMin1][iMin2];
            nClusters--;
            double fSep1 = fSeparations[iMin1];
            double fSep2 = fSeparations[iMin2];
            double fDist1 = (0.5 * fMinDistance) + (0.5 * (fSep1 - fSep2));
            double fDist2 = (0.5 * fMinDistance) + (0.5 * (fSep2 - fSep1));
            if (nClusters > 2) {
                double fNewSeparationSum = 0;
                double fMutualDistance = fDist[iMin1][iMin2];
                double[] fRow1 = fDist[iMin1];
                double[] fRow2 = fDist[iMin2];
                for (int i = 0; i < n; i++) {
                    if (i == iMin1 || i == iMin2 || nClusterID[i].size() == 0) {
                        fRow1[i] = 0;
                    } else {
                        double fVal1 = fRow1[i];
                        double fVal2 = fRow2[i];
                        double fDistance = (fVal1 + fVal2 - fMutualDistance) / 2.0;
                        fNewSeparationSum += fDistance;
                        fSeparationSums[i] += (fDistance - fVal1 - fVal2);
                        fSeparations[i] = fSeparationSums[i] / (nClusters - 2);
                        fRow1[i] = fDistance;
                        fDist[i][iMin1] = fDistance;
                    }
                }
                fSeparationSums[iMin1] = fNewSeparationSum;
                fSeparations[iMin1] = fNewSeparationSum / (nClusters - 2);
                fSeparationSums[iMin2] = 0;
                merge(iMin1, iMin2, fDist1, fDist2, nClusterID, clusterNodes);
                int iPrev = iMin2;
                while (nClusterID[iPrev].size() == 0) {
                    iPrev--;
                }
                nNextActive[iPrev] = nNextActive[iMin2];
            } else {
                merge(iMin1, iMin2, fDist1, fDist2, nClusterID, clusterNodes);
                break;
            }
        }
        for (int i = 0; i < n; i++) {
            if (nClusterID[i].size() > 0) {
                for (int j = i + 1; j < n; j++) {
                    if (nClusterID[j].size() > 0) {
                        double fDist1 = fDist[i][j];
                        if (nClusterID[i].size() == 1) {
                            merge(i, j, fDist1, 0, nClusterID, clusterNodes);
                        } else if (nClusterID[j].size() == 1) {
                            merge(i, j, 0, fDist1, nClusterID, clusterNodes);
                        } else {
                            merge(i, j, fDist1 / 2.0, fDist1 / 2.0, nClusterID, clusterNodes);
                        }
                        break;
                    }
                }
            }
        }
    }
