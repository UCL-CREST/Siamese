    public static double[][] mattrans(double[][] mat1, int mat1r, int mat1c) {
        int row, col;
        double[][] mat2 = new double[mat1c][mat1r];
        for (row = 0; row < mat1r; row++) {
            for (col = 0; col < mat1c; col++) mat2[col][row] = mat1[row][col];
        }
        return mat2;
    }
