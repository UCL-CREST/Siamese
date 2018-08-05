    public static double[][] transpose(double[][] a) {
        int rows = a.length;
        int cols = a[0].length;
        double[][] res = new double[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res[j][i] = a[i][j];
            }
        }
        return res;
    }
