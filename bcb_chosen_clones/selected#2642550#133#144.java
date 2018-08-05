    public final void transpose() {
        if (!isSquare()) {
            throw new RuntimeException("Cannot transpose no square matrix!");
        }
        for (int row = 0; row < height_; row++) {
            for (int col = row + 1; col < width_; col++) {
                double temp = data_[row][col];
                data_[row][col] = data_[col][row];
                data_[col][row] = temp;
            }
        }
    }
