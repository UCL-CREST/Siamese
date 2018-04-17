    public void initDct8x8() {
        int i = 0;
        int j = 0;
        double sqJpeg = Math.sqrt((double) NJPEG);
        double sqJpeg2 = Math.sqrt(2.0 / NJPEG);
        for (j = 0; j < NJPEG; j++) {
            C[0][j] = 1.0 / sqJpeg;
            Ct[j][0] = C[0][j];
        }
        for (i = 1; i < NJPEG; i++) {
            for (j = 0; j < NJPEG; j++) {
                C[i][j] = sqJpeg2 * Math.cos(Math.PI * (2 * j + 1) * i / (2.0 * NJPEG));
                Ct[j][i] = C[i][j];
            }
        }
    }
