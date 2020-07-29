    public static boolean isSymmetric(double[][] A) throws Exception {
        debug("A = ");
        debug(Matlab.MatrixToString(new Matrix(A)));
        int N = A.length;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < i; j++) {
                if (A[i][j] == Double.NaN || A[j][i] == Double.NaN) {
                    Logger.println("Error in Cholesky: Matrix contains NaN");
                    debug("A = ");
                    debug(Matlab.MatrixToString(new Matrix(A)));
                    throw new Exception("Matrix contains NaN");
                }
                if (A[i][j] - A[j][i] > EPSILON) return false; else A[i][j] = A[j][i];
            }
        }
        return true;
    }
