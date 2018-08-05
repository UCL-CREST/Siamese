    private static long[][] transposeMatrix(long[][] A) {
        long[][] result = cloneMatrix(A);
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                result[i][j] = A[j][i];
            }
        }
        return result;
    }
