    public static DistanceMatrix getSimilarityMatrixFromAlignment(Alignment alignment) {
        int nSites = alignment.getSiteCount();
        int nTaxa = alignment.getSequenceCount();
        double[][] ibs = new double[nTaxa][nTaxa];
        int[][] cellCounts = new int[nTaxa][nTaxa];
        for (int t1 = 0; t1 < nTaxa; t1++) {
            for (int s = 0; s < nSites; s++) {
                byte t1base = alignment.getBase(t1, s);
                if (t1base != Alignment.UNKNOWN_DIPLOID_ALLELE) {
                    ibs[t1][t1] += AlignmentUtils.getDiploidIdentity(t1base, t1base);
                    cellCounts[t1][t1]++;
                    for (int t2 = t1 + 1; t2 < nTaxa; t2++) {
                        byte t2base = alignment.getBase(t2, s);
                        if (t2base != Alignment.UNKNOWN_DIPLOID_ALLELE) {
                            ibs[t1][t2] += AlignmentUtils.getDiploidIdentity(t1base, t2base);
                            cellCounts[t1][t2]++;
                        }
                    }
                }
            }
        }
        for (int t1 = 0; t1 < nTaxa; t1++) {
            ibs[t1][t1] /= 2 * cellCounts[t1][t1];
            for (int t2 = t1 + 1; t2 < nTaxa; t2++) {
                ibs[t1][t2] /= 2 * cellCounts[t1][t2];
                ibs[t2][t1] = ibs[t1][t2];
            }
        }
        DistanceMatrix dm = new DistanceMatrix(ibs, alignment.getIdGroup());
        return dm;
    }
