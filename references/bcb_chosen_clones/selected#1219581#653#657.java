    public static final double[][] transpose(final double m1[][]) {
        double[][] m3 = new double[m1[0].length][m1.length];
        for (int ii = 0; ii < m1.length; ii++) for (int jj = 0; jj < m1[ii].length; jj++) m3[jj][ii] = m1[ii][jj];
        return m3;
    }
