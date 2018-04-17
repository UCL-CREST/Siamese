    public static final float[][] transposeMatrix(float[][] a) {
        int ra = a.length;
        int ca = a[0].length;
        float[][] m = new float[ca][ra];
        for (int i = 0; i < ra; i++) {
            for (int j = 0; j < ca; j++) m[j][i] = a[i][j];
        }
        return m;
    }
