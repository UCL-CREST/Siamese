        private double[][] calculateSimilarityMatrix(MSA ma, String matrixName) {
            SubstitutionMatrixFactory factory = SubstitutionMatrixFactory.getSubstitutionMatrixFactory();
            double simmat[][] = new double[ma.getSeqCount()][ma.getSeqCount()];
            double maxk = (double) (simmat.length + 1.0) * ((double) simmat.length / 2.0);
            Hashtable<Character, Integer> letter2index = SubstitutionMatrixFactory.getSymbolMap();
            Short matrix[][] = factory.getMatrix(matrixName);
            Short matrix2[][] = new Short[matrix.length][matrix[matrix.length - 1].length];
            for (int i = 0; i < matrix2.length; i++) for (int j = 0; j < matrix2[i].length; j++) if (j <= i) matrix2[i][j] = matrix[i][j]; else matrix2[i][j] = matrix[j][i];
            int k = 0;
            Sequence seqi;
            for (int i = 0; i < simmat.length; i++) {
                seqi = ma.getSequence(i);
                simmat[i][i] = (int) AlignmentToolkit.scorePair(seqi, seqi, 10, 10, matrix2, letter2index);
                if (++k % 250 == 0) progressBar.setValue((int) ((double) k / maxk * 100.0));
            }
            Sequence seqj;
            for (int i = 0; i < simmat.length; i++) {
                seqi = ma.getSequence(i);
                for (int j = i + 1; j < simmat.length; j++) {
                    seqj = ma.getSequence(j);
                    simmat[i][j] = (int) AlignmentToolkit.scorePair(seqi, seqj, 10, 10, matrix2, letter2index) / Math.sqrt(simmat[i][i] * simmat[j][j]);
                    simmat[j][i] = simmat[i][j];
                    if (simmat[i][j] < minVal) minVal = simmat[i][j];
                    if (simmat[i][j] > maxVal) maxVal = simmat[i][j];
                    if (++k % 250 == 0) {
                        progressBar.setValue((int) ((double) k / maxk * 100.0));
                    }
                }
            }
            for (int i = 0; i < simmat.length; i++) simmat[i][i] = 1;
            if (1 > maxVal) maxVal = 1;
            return simmat;
        }
