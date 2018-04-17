    private static double[][] getDistances(List<Sequence> seqs, PairwiseAligner aligner, TreeBuilderFactory.DistanceModel model, final ProgressListener progressListener) throws CannotBuildDistanceMatrixException {
        final int n = seqs.size();
        double[][] d;
        final long memoryRequired = ((long) n) * n * 8;
        if (n > 100) {
            final long maxMemory = Runtime.getRuntime().maxMemory();
            if (memoryRequired > maxMemory) {
                throw new CannotBuildDistanceMatrixException(getNotEnoughMemoryMessage(memoryRequired));
            }
        }
        try {
            d = new double[n][n];
        } catch (OutOfMemoryError e) {
            throw new CannotBuildDistanceMatrixException(getNotEnoughMemoryMessage(memoryRequired));
        }
        CompositeProgressListener compositeProgressListener = new CompositeProgressListener(progressListener, getProgressIncrements(n));
        for (int i = 0; i < n; ++i) {
            compositeProgressListener.beginSubtask();
            CompositeProgressListener subComposite = new CompositeProgressListener(compositeProgressListener, (n - (i + 1)) * 2);
            for (int j = i + 1; j < n; ++j) {
                subComposite.beginSubtask();
                PairwiseAligner.Result result = aligner.doAlignment(seqs.get(i), seqs.get(j), subComposite);
                if (progressListener.isCanceled()) return d;
                subComposite.beginSubtask();
                BasicDistanceMatrix matrix;
                switch(model) {
                    case F84:
                        matrix = new F84DistanceMatrix(result.alignment, subComposite);
                        break;
                    case HKY:
                        matrix = new HKYDistanceMatrix(result.alignment, subComposite);
                        break;
                    case TamuraNei:
                        matrix = new TamuraNeiDistanceMatrix(result.alignment, subComposite);
                        break;
                    case JukesCantor:
                    default:
                        matrix = new JukesCantorDistanceMatrix(result.alignment, subComposite);
                }
                d[i][j] = matrix.getDistances()[0][1];
                d[j][i] = d[i][j];
            }
        }
        return d;
    }
