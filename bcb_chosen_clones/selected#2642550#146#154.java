    public final Matrix getTranspose() {
        double[][] newData = new double[width_][height_];
        for (int row = 0; row < height_; row++) {
            for (int col = 0; col < width_; col++) {
                newData[col][row] = data_[row][col];
            }
        }
        return new Matrix(newData);
    }
