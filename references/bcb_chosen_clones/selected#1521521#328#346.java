    public float[][] calSolDominance() {
        float[][] dom = new float[nsol][nsol];
        float[][] int2 = new float[nsol][ntp];
        for (int i = 0; i < nsol; i++) {
            for (int j = 0; j < ntp; j++) {
                int2[i][j] = interaction[j][i];
            }
        }
        for (int i = 0; i < nsol; i++) {
            for (int j = (i + 1); j < nsol; j++) {
                dom[i][j] = (float) checkDominance(int2[i], int2[j]);
                if (dom[i][j] == 1) {
                    dom[j][i] = -1;
                    dom[i][j] = 0;
                }
            }
        }
        return (dom);
    }
