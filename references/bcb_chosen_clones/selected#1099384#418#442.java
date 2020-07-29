    public static IDataGrid transpose(IDataGrid grid) {
        IDataGrid gridT;
        double[][] a = GridUtils.grid2Array(grid);
        int rows = a.length;
        int cols = a[0].length;
        if (rows == cols) {
            for (int i = 0; i < a.length; i++) {
                for (int j = i + 1; j < a[0].length; j++) {
                    double tmp = a[i][j];
                    a[i][j] = a[j][i];
                    a[j][i] = tmp;
                }
            }
            gridT = GridUtils.doubleArrayToGrid(a);
        } else {
            double[][] tmp = new double[cols][rows];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    tmp[j][i] = a[i][j];
                }
            }
            gridT = GridUtils.doubleArrayToGrid(tmp);
        }
        return gridT;
    }
