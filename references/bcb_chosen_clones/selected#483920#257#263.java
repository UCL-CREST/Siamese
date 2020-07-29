    public static void transpose(double[][] A, double[][] B) {
        assert rows(B) == cols(A);
        assert cols(B) == rows(A);
        int rows = rows(B);
        int cols = cols(B);
        for (int i = 0; i < rows; i++) for (int j = 0; j < cols; j++) B[i][j] = A[j][i];
    }
