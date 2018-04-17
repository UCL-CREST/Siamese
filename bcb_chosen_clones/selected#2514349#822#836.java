    public static double[][] transpose(final double[][] _matrix) {
        if (_matrix == null) {
            return null;
        }
        if (_matrix.length == 0) {
            return new double[0][0];
        }
        final double[][] res = new double[_matrix[0].length][_matrix.length];
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res[i].length; j++) {
                res[i][j] = _matrix[j][i];
            }
        }
        return res;
    }
