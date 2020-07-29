    public static double[][] transpose(double matrix[][], boolean inPlace) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        if (!inPlace) {
            double result[][] = new double[cols][rows];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    result[j][i] = matrix[i][j];
                }
            }
            return result;
        }
        if (rows != cols) {
            System.out.println("Transpose in place requires a square matrix");
        } else {
            double dum;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < i; j++) {
                    dum = matrix[i][j];
                    matrix[i][j] = matrix[j][i];
                    matrix[j][i] = dum;
                }
            }
        }
        return matrix;
    }
