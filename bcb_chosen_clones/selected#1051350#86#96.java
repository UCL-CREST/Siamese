    public static final double[][] transpose(double[][] a) {
        int am = a.length;
        int an = a[0].length;
        double[][] result = new double[an][am];
        for (int i = 0; i < am; i++) {
            for (int j = 0; j < an; j++) {
                result[j][i] = a[i][j];
            }
        }
        return result;
    }
