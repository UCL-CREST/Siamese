    private void initMatrix() {
        for (int j = 0; j < 8; j++) {
            double nn = (double) (8);
            c[0][j] = 1.0 / Math.sqrt(nn);
            cT[j][0] = c[0][j];
        }
        for (int i = 1; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                double jj = (double) j;
                double ii = (double) i;
                c[i][j] = Math.sqrt(2.0 / 8.0) * Math.cos(((2.0 * jj + 1.0) * ii * Math.PI) / (2.0 * 8.0));
                cT[j][i] = c[i][j];
            }
        }
    }
