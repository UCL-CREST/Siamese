    public static double[][] transpose(double[][] M) {
        int lines = M.length;
        int columns = M[0].length;
        double[][] Mtrans = new double[columns][lines];
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < columns; j++) {
                Mtrans[j][i] = M[i][j];
            }
        }
        return Mtrans;
    }
