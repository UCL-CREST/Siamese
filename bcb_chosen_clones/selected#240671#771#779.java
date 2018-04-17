    public static double[][] transpose(double[][] mat) {
        double[][] a = new double[mat[0].length][mat.length];
        for (int i = 0; i < mat[0].length; i++) {
            for (int j = 0; j < mat.length; j++) {
                a[i][j] = mat[j][i];
            }
        }
        return a;
    }
