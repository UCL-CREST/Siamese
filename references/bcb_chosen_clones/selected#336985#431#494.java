    void doLinkClustering(int nClusters, Vector<Integer>[] nClusterID, Node[] clusterNodes) {
        int nInstances = m_instances.numInstances();
        PriorityQueue<Tuple> queue = new PriorityQueue<Tuple>(nClusters * nClusters / 2, new TupleComparator());
        double[][] fDistance0 = new double[nClusters][nClusters];
        double[][] fClusterDistance = null;
        if (m_bDebug) {
            fClusterDistance = new double[nClusters][nClusters];
        }
        for (int i = 0; i < nClusters; i++) {
            fDistance0[i][i] = 0;
            for (int j = i + 1; j < nClusters; j++) {
                fDistance0[i][j] = getDistance0(nClusterID[i], nClusterID[j]);
                fDistance0[j][i] = fDistance0[i][j];
                queue.add(new Tuple(fDistance0[i][j], i, j, 1, 1));
                if (m_bDebug) {
                    fClusterDistance[i][j] = fDistance0[i][j];
                    fClusterDistance[j][i] = fDistance0[i][j];
                }
            }
        }
        while (nClusters > m_nNumClusters) {
            int iMin1 = -1;
            int iMin2 = -1;
            if (m_bDebug) {
                double fMinDistance = Double.MAX_VALUE;
                for (int i = 0; i < nInstances; i++) {
                    if (nClusterID[i].size() > 0) {
                        for (int j = i + 1; j < nInstances; j++) {
                            if (nClusterID[j].size() > 0) {
                                double fDist = fClusterDistance[i][j];
                                if (fDist < fMinDistance) {
                                    fMinDistance = fDist;
                                    iMin1 = i;
                                    iMin2 = j;
                                }
                            }
                        }
                    }
                }
                merge(iMin1, iMin2, fMinDistance, fMinDistance, nClusterID, clusterNodes);
            } else {
                Tuple t;
                do {
                    t = queue.poll();
                } while (t != null && (nClusterID[t.m_iCluster1].size() != t.m_nClusterSize1 || nClusterID[t.m_iCluster2].size() != t.m_nClusterSize2));
                iMin1 = t.m_iCluster1;
                iMin2 = t.m_iCluster2;
                merge(iMin1, iMin2, t.m_fDist, t.m_fDist, nClusterID, clusterNodes);
            }
            for (int i = 0; i < nInstances; i++) {
                if (i != iMin1 && nClusterID[i].size() != 0) {
                    int i1 = Math.min(iMin1, i);
                    int i2 = Math.max(iMin1, i);
                    double fDistance = getDistance(fDistance0, nClusterID[i1], nClusterID[i2]);
                    if (m_bDebug) {
                        fClusterDistance[i1][i2] = fDistance;
                        fClusterDistance[i2][i1] = fDistance;
                    }
                    queue.add(new Tuple(fDistance, i1, i2, nClusterID[i1].size(), nClusterID[i2].size()));
                }
            }
            nClusters--;
        }
    }
