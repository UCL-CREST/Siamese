    protected static float[][] trans(float[][] a) {
        int am = a.length;
        int an = a[0].length;
        float[][] t = new float[an][am];
        for (int i = 0; i < am; i++) {
            for (int j = 0; j < an; j++) {
                t[j][i] = a[i][j];
            }
        }
        return t;
    }
