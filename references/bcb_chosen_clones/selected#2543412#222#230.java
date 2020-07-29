    public double[][] transpose(double[][] d) {
        double[][] result = new double[d[0].length][d.length];
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[i].length; j++) {
                result[j][i] = d[i][j];
            }
        }
        return result;
    }
