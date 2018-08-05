    public static double[][] transpose(double[][] M) {
        double[][] tM = new double[M[0].length][M.length];
        for (int i = 0; i < tM.length; i++) {
            for (int j = 0; j < tM[0].length; j++) {
                tM[i][j] = M[j][i];
            }
        }
        return tM;
    }
