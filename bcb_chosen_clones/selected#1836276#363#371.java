    public Matrix transpose() {
        int n = rows();
        int m = columns();
        double[][] newComponents = new double[m][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) newComponents[j][i] = components[i][j];
        }
        return new Matrix(newComponents);
    }
