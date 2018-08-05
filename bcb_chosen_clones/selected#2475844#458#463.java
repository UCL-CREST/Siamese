    public GIMatrix transpose() throws BadMatrixFormatException {
        if (m != n) throw new BadMatrixFormatException();
        double[][] transpose = new double[array.length][array[0].length];
        for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) transpose[i][j] = array[j][i];
        return new GIMatrix(transpose);
    }
