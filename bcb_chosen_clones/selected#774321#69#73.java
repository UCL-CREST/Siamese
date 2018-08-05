    static double[][] transpose(double[][] m) {
        double[][] t = matrix(nbColumns(m), nbRows(m));
        for (int r = 0; r < nbRows(m); r++) for (int c = 0; c < nbColumns(m); c++) t[c][r] = m[r][c];
        return t;
    }
