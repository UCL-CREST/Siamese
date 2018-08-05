    public static double[][] transpose(double[][] M) {
        double[][] O = new double[M[0].length][M.length];
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[0].length; j++) {
                O[j][i] = M[i][j];
            }
        }
        return O;
    }
