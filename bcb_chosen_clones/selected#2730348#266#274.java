    public static double[][] mergeColumns(double[]... x) {
        double[][] array = new double[x[0].length][x.length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                array[i][j] = x[j][i];
            }
        }
        return array;
    }
