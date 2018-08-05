    public RealMatrix getR() {
        if (cachedR == null) {
            final int n = qrt.length;
            final int m = qrt[0].length;
            double[][] ra = new double[m][n];
            for (int row = FastMath.min(m, n) - 1; row >= 0; row--) {
                ra[row][row] = rDiag[row];
                for (int col = row + 1; col < n; col++) {
                    ra[row][col] = qrt[col][row];
                }
            }
            cachedR = MatrixUtils.createRealMatrix(ra);
        }
        return cachedR;
    }
