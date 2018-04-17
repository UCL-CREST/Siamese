    public static double[][] invert(double[][] matrix) {
        double[][] result = new double[matrix[0].length][matrix.length];
        for (int row = 0; row < matrix.length; row++) for (int col = 0; col < matrix[row].length; col++) result[col][row] = result[row][col];
        return result;
    }
