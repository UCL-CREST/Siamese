    public Matrix transpose() {
        Matrix output = new Matrix(cols, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                output.contents[j][i] = contents[i][j];
            }
        }
        return output;
    }
