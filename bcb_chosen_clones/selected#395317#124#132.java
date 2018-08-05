    public static double[][] transposeMatrix(double[][] vector) {
        double[][] tm = new double[vector[0].length][vector.length];
        for (int i = 0; i < tm.length; i++) {
            for (int j = 0; j < tm[i].length; j++) {
                tm[i][j] = vector[j][i];
            }
        }
        return tm;
    }
