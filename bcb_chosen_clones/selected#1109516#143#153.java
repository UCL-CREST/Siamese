    public static double[][] transposed(double[][] matrix) {
        int row = matrix[0].length;
        int line = matrix.length;
        double[][] ans = new double[row][line];
        for (int i = 0; i < line; i++) {
            for (int j = 0; j < row; j++) {
                ans[j][i] = matrix[i][j];
            }
        }
        return ans;
    }
