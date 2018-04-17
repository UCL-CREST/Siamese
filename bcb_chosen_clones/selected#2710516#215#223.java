    public ZnMatrix transposed() {
        ZnMatrix m = new ZnMatrix(columns, rows, modulus);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                m.coefficients[c][r] = coefficients[r][c];
            }
        }
        return m;
    }
