    public float[][] findDistances() {
        float[][] distance = new float[noseqs][noseqs];
        if (pwtype.equals("PID")) {
            for (int i = 0; i < noseqs - 1; i++) {
                for (int j = i; j < noseqs; j++) {
                    if (j == i) {
                        distance[i][i] = 0;
                    } else {
                        distance[i][j] = 100 - Comparison.compare(sequence[i], sequence[j]);
                        distance[j][i] = distance[i][j];
                    }
                }
            }
        } else {
            float max = -1;
            for (int i = 0; i < noseqs - 1; i++) {
                for (int j = i; j < noseqs; j++) {
                    AlignSeq as = new AlignSeq(sequence[i], sequence[j], "pep");
                    as.calcScoreMatrix();
                    as.traceAlignment();
                    as.printAlignment();
                    distance[i][j] = (float) as.maxscore;
                    if (max < distance[i][j]) {
                        max = distance[i][j];
                    }
                }
            }
            for (int i = 0; i < noseqs - 1; i++) {
                for (int j = i; j < noseqs; j++) {
                    distance[i][j] = max - distance[i][j];
                    distance[j][i] = distance[i][j];
                }
            }
        }
        return distance;
    }
