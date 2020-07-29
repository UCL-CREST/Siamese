    public static double[][] transpose(double[][] a) {
        double[][] c = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 3; k++) {
                c[i][k] = a[k][i];
            }
        }
        return c;
    }
