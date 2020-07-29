    public static double[][] transpose(double[][] matrix) throws ICParameterException {
        double[][] r;
        if (matrix == null) {
            r = null;
        } else {
            r = new double[matrix[0].length][matrix.length];
            for (int i = 0; i < matrix.length; i++) for (int j = 0; j < matrix[0].length; j++) r[j][i] = matrix[i][j];
        }
        return r;
    }
