        synchronized double[][] getDistances(Alignment alignment, ProgressListener progress) {
            this.alignment = alignment;
            final int stateCount = alignment.getSequenceType().getCanonicalStateCount();
            if (stateCount != 4) {
                throw new IllegalArgumentException("F84DistanceMatrix must have nucleotide patterns");
            }
            int dimension = alignment.getTaxa().size();
            double[][] distances = new double[dimension][dimension];
            float tot = (dimension * (dimension - 1)) / 2;
            int done = 0;
            for (int i = 0; i < dimension; ++i) {
                for (int j = i + 1; j < dimension; ++j) {
                    distances[i][j] = calculatePairwiseDistance(i, j);
                    distances[j][i] = distances[i][j];
                    if (progress != null) progress.setProgress(++done / tot);
                }
            }
            return distances;
        }
