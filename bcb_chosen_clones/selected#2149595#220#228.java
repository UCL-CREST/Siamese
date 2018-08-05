    private double[][] getInputColumn(double[][] vectors) {
        double[][] columns = new double[vectors[0].length][vectors.length];
        for (int i = 0; i < columns.length; i++) {
            for (int j = 0; j < columns[0].length; j++) {
                columns[i][j] = vectors[j][i];
            }
        }
        return columns;
    }
