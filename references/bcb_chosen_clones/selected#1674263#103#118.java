    public static double[][] transpose(double[][] data) {
        double[][] dataTransposed = new double[0][0];
        if (data != null) {
            int numRows = data.length;
            if (numRows > 0) {
                int numCols = data[0].length;
                dataTransposed = new double[numCols][numRows];
                for (int i = 0; i < numRows; i++) {
                    for (int j = 0; j < numCols; j++) {
                        dataTransposed[j][i] = data[i][j];
                    }
                }
            }
        }
        return dataTransposed;
    }
