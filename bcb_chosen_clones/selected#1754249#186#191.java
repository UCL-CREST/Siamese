    public static final double[][] m3t(double A[][]) {
        double R[][] = new double[3][3];
        int i, j;
        for (i = 0; i < 3; i++) for (j = 0; j < 3; j++) R[i][j] = A[j][i];
        return (R);
    }
