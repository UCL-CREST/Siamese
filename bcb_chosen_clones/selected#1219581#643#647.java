    public static final int[][] transpose(final int m1[][]) {
        int[][] m3 = new int[m1[0].length][m1.length];
        for (int ii = 0; ii < m1.length; ii++) for (int jj = 0; jj < m1[ii].length; jj++) m3[jj][ii] = m1[ii][jj];
        return m3;
    }
