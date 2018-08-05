    public static double[][] transpose(double[][] values) {
        double[][] swapValues = new double[values[0].length][values.length];
        for (int x = 0; x < values.length; x++) {
            for (int y = 0; y < values[x].length; y++) {
                swapValues[y][x] = values[x][y];
            }
        }
        return swapValues;
    }
