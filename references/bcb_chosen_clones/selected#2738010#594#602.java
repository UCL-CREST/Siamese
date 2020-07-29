    public void transpose() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < i; j++) {
                double t = flmat[i][j];
                flmat[i][j] = flmat[j][i];
                flmat[j][i] = t;
            }
        }
    }
