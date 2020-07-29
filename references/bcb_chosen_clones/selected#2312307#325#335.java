    public static Double[][] transposeMatrix(Double[][] m) {
        int r = m.length;
        int c = m[0].length;
        Double[][] t = new Double[c][r];
        for (int i = 0; i < r; ++i) {
            for (int j = 0; j < c; ++j) {
                t[j][i] = m[i][j];
            }
        }
        return t;
    }
