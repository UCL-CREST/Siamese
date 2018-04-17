    public static double[][] invertMatrix(double[][] matrix) {
        int firstDimentionOfResultMatrix = matrix[0].length;
        int secondDimentionOfResultMatrix = matrix.length;
        double[][] resultMatrix = new double[firstDimentionOfResultMatrix][secondDimentionOfResultMatrix];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                resultMatrix[j][i] = matrix[i][j];
            }
        }
        return resultMatrix;
    }
