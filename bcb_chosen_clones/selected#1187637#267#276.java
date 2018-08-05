    public static Matrix transpose(final Matrix input) {
        final double[][] transposeMatrix = new double[input.getCols()][input.getRows()];
        final double[][] d = input.getData();
        for (int r = 0; r < input.getRows(); r++) {
            for (int c = 0; c < input.getCols(); c++) {
                transposeMatrix[c][r] = d[r][c];
            }
        }
        return new Matrix(transposeMatrix);
    }
