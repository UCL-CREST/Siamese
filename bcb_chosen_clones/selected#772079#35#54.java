    static double[][] getDistances(List<Sequence> seqs, PairwiseAligner aligner, final ProgressListener progress) {
        final int n = seqs.size();
        double[][] d = new double[n][n];
        boolean isProtein = seqs.get(0).getSequenceType().getCanonicalStateCount() > 4;
        CompoundAlignmentProgressListener compoundProgress = new CompoundAlignmentProgressListener(progress, (n * (n - 1)) / 2);
        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                PairwiseAligner.Result result = aligner.doAlignment(seqs.get(i), seqs.get(j), compoundProgress.getMinorProgress());
                compoundProgress.incrementSectionsCompleted(1);
                if (compoundProgress.isCancelled()) return d;
                if (isProtein) {
                    d[i][j] = new JukesCantorDistanceMatrix(result.alignment, null).getDistances()[0][1];
                } else {
                    d[i][j] = new F84DistanceMatrix(result.alignment).getDistances()[0][1];
                }
                d[j][i] = d[i][j];
            }
        }
        return d;
    }
