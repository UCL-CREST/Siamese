    private double[][] getTransponColumns(double[][] data) {
        double[][] columns = new double[data[0].length][data.length];
        for (int i = 0; i < columns.length; i++) {
            for (int j = 0; j < columns[0].length; j++) {
                columns[i][j] = data[j][i];
            }
        }
        return columns;
    }
