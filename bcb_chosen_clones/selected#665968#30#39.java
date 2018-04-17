    public static int[][] transpose(int[][] matrix) throws ICParameterException {
        int[][] r;
        if (matrix == null) {
            r = null;
        } else {
            r = new int[matrix[0].length][matrix.length];
            for (int i = 0; i < matrix.length; i++) for (int j = 0; j < matrix[0].length; j++) r[j][i] = matrix[i][j];
        }
        return r;
    }
