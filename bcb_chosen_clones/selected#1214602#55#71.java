    public void calculateDistanceMatrix(float[][] d) {
        data = new float[n2][n2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                data[i][j] = d[i][j];
                data[j][i] = d[i][j];
            }
        }
        for (int i = 0; i < n; i++) {
            data[n][i] = endMisMatch;
            data[i][n] = endMisMatch;
        }
        for (int i = 0; i < n + 1; i++) {
            data[n + 1][i] = endMisMatch;
            data[i][n + 1] = endMisMatch;
        }
    }
