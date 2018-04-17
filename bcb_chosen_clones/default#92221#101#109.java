    private static int[][] transpose(int[][] matrix) {
        int[][] transposed = new int[matrix.length][matrix[0].length];
        for (int k = 0; k < matrix.length; k++) {
            for (int j = 0; j < matrix.length; j++) {
                transposed[j][k] = matrix[k][j];
            }
        }
        return transposed;
    }
